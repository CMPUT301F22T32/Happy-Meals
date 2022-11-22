package com.example.happymeals.mealplan;

import android.util.Log;

import com.example.happymeals.Constants;
import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.database.FireStoreManager;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author jeastgaa
 * @version 1.00.01
 * @see DatabaseObject The parent object for this class which allows it to be stored inside the
 * firebase databse using the {@link FireStoreManager}
 * This class represents Meal Plans that can be loaded inside the application and shown to the
 * end user. These meal plans will always be stored in the database as a week/week schema. This
 * means that the meal plan should be defined with the date as a Sunday value otherwise the
 * data will not turn out.
 */
public class MealPlanStorage implements DatabaseListener {

    private static MealPlanStorage instance = null;

    private ArrayList<MealPlan> mealPlans;

    private DatasetWatcher listeningActivity;
    private CollectionReference mealPlanCollection;
    private FireStoreManager fsm;
    
    public MealPlanStorage() {
        mealPlans = new ArrayList<>();
        this.listeningActivity = null;
        this.fsm = FireStoreManager.getInstance();
        this.mealPlanCollection = fsm.getCollectionReferenceTo( Constants.COLLECTION_NAME.MEAL_PLANS );
        updateMealPlansFromDatabase();
    }

    public static MealPlanStorage getInstance() {
        if (instance == null)
            instance = new MealPlanStorage();
        return instance;
    }

    /**
     * This will grab all the MealPlan values stored in the database.
     * This classes OnDataFetchSuccess method will handle the response on a successful data fetch.
     */
    public void updateMealPlansFromDatabase() {
        fsm.getAllFrom( mealPlanCollection, this, new MealPlan() );
    }

    /**
     * Links a {@link DatasetWatcher} instance to this class so data fetching can return results.
     * This should be set/called any time a new class is watching the storage contents and need
     * to be updated on a change.
     * @param listener The {@link DatasetWatcher} implementation responsible for dealing with
     *                 a change in the dataset.
     */
    public void setListeningActivity( DatasetWatcher listener ) {
        this.listeningActivity = listener;
    }

    /**
     * This will send a signal to the class which is currently linked to the storage
     * that the dataset has changed.
     */
    public void updateStorage() {
        if (listeningActivity != null) {
            listeningActivity.signalChangeToAdapter();
        } else {
            Log.d( "MealPlan Storage Error: ", "No listening activity was defined"
                    + "Cannot update adapter.", null );
        }
    }

    /**
     * This returns an ArrayList of the MealPlans
     * @return
     * Returns the ArrayList of MealPlans that the MealPlanStorage has
     */
    public ArrayList< MealPlan > getMealPlans() {
        return mealPlans;
    }

    /**
     * This add and MealPlan to MealPlans and adds the MealPlan to the Firebase database
     * @param mealPlan
     */
    public void addMealPlan( MealPlan mealPlan ) {
        mealPlans.add( mealPlan );
        updateStorage();
        fsm.addData(mealPlanCollection, mealPlan);
    }

    /**
     * This will return a specific {@link MealPlan} given the name.
     * @param mealPlanName The {@link String} which holds the name of the requested
     * {@link MealPlan}.
     * @return The {@link MealPlan} object.
     */
    public MealPlan getMealPlan( String mealPlanName ) {
        for( MealPlan mealPlan : mealPlans ) {
            if ( mealPlan.getName().equals( mealPlanName ) ) {
                return mealPlan;
            }
        }
        return null;
    }

    public MealPlan getMealPlanForDay(Date date) {
        MealPlan mealplan = null;
        for (MealPlan mp : mealPlans) {
            if (mp.isWithinDate(date))
                return mp;
        }
        return null;
    }

    /**
     * This will remove the provided {@link MealPlan} from the database and this classes
     * storage {@link ArrayList}.
     * @param mealPlan The {@link MealPlan} that is being removed form storage.
     */
    public void removeMealPlan( MealPlan mealPlan ) {
        mealPlans.remove( mealPlan );
        updateStorage();
        fsm.deleteDocument( mealPlanCollection, mealPlan );
    }

    /**
     * Given an {@link MealPlan} this will go through all the stored MealPlans and upon
     * matching the name of the passed {@link MealPlan} it will replace that object with
     * the given one. If it cannot be found then a Log message will be produced.
     * @param mealPlan The {@link MealPlan} which is being updated in the storage.
     */
    public void updateMealPlan( MealPlan mealPlan ) {
        for( MealPlan storedMealPlan : mealPlans ) {
            if( storedMealPlan.getName().equals(mealPlan.getName() ) ){
                storedMealPlan = mealPlan;
                updateStorage();
                fsm.updateData( mealPlanCollection, mealPlan );
                return;
            }
        }
        Log.d("MealPlan Storage:", "An MealPlan update was requested on:\n"
                + mealPlan.getName() + ", but no stored MealPlan could be found", null );

    }

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        MealPlan mealPlan = ( MealPlan ) data;
        Boolean replace = Boolean.FALSE;
        // Loop through the list of MealPlans and see if we are adding a new one
        // or updating a pre-existing one.
        for( MealPlan storedMealPlan : mealPlans ) {
            if(storedMealPlan.getName().equals(mealPlan.getName())){
                storedMealPlan = mealPlan;
                replace = Boolean.TRUE;
                break;
            }
        }
        if( !replace ){
            mealPlans.add( mealPlan );
        }
        updateStorage();
    }

    @Override
    public void onSpinnerFetchSuccess(Map<String, Object> data) {

    }
}
