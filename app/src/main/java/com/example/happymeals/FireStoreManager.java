package com.example.happymeals;

import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author jeastgaa
 * @version 0.00.12
 * @see DatabaseListener As used interface to subscribe to successful data requests.
 * FireStoreManager is responsible for instantiating a connectino to the database as well as
 * getting and setting requested values in the databse. Contains global {@link DocumentReference}
 * which holds the documents for representing the users collections of data.
 */
public class FireStoreManager {

    // <todo> Move enumerations over to a constants class.

    private DocumentReference userDocument;

    private static final String IP_TAG = "IpFetcher";
    private final String GET_DATA_TAG = "Data Request";
    private final String DATA_STORE_TAG = "Data Store";
    private final String DATA_DELETE_TAG = "Data Removal";

    /**
     * Class constructor. This will connect to the Firebase database. Finding the local IP address
     * of the device being used as the document name that will hold user's collections.
     */
    public FireStoreManager() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        // Get the local IP address to identify the user.
        String ipAddress = getLocalIpAddress();
        // <todo> Need to make string a constant in a constant file.
        CollectionReference collectionReference = database.collection( "localUsers" );
        userDocument = collectionReference.document( ipAddress );
    }

    /**
     * Requires a collection name and document name which will lead to a specific data set.
     * addData() will set the document's data to the the arrtibutes of the given object.
     * The attributes given must be from a getAttrName() method or Firestore will not be able to
     * translate it to a document. This will override any pre-existing data in the request document.
     * @param collectionName The {@link Enum} of the collection which holds desired document.
     * @param documentName Document namme represented as a {@link String} which data is being
     *                     written too.
     * @param data {@link DatabaseObject} holding data to be stored in the document.
     */
    public void addData( Constants.COLLECTION_NAME collectionName, String documentName, DatabaseObject data ) {
        userDocument.collection( collectionName.toString() )
                .document( documentName )
                .set( data )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d( DATA_STORE_TAG, "Data has been created." );
                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d( DATA_STORE_TAG, "Data could not be created." );
                    }
                });
    }

    /**
     * Requires a collection name and document name which will lead to a specific dataset.
     * deleteDocument() will remove the requested document and all it's entries from the database.
     * If no such document exists but the path specified is valid the request will still be
     * successful.
     * @param collectionName The {@link Enum} of the collection name that holds all data entries.
     * @param documentName The {@link String} of the document name which should hold the specific
     *                     data entry to be deleted.
     */
    public void deleteDocument( Constants.COLLECTION_NAME collectionName, String documentName ) {
        userDocument.collection( collectionName.toString() ).document( documentName )
                .delete()
                .addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d( DATA_DELETE_TAG, "Data has been removed." );
                    }
                })
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d( DATA_DELETE_TAG, "Data was unable to be removed." );
                    }
                });

    }

    /**
     * Finds the document located in the given path and calls the listener function. At this time
     * the proper class will be created from the fetched data and passed as a parameter.
     * @param collectionName The {@link Enum} representing the collection name where the document
     *                       is located.
     * @param documentName The {@link String} representing the document name that is being removed
     *                     from the given collection.
     * @param listener The {@link Class} which impliments the {@link DatabaseListener}
     *                 and holds the listener for the success of data fetching.
     * @param requestClassType The {@link DatabaseObject} child class specifying which type of class
     *                         should be created and returned.
     * @see <todo> List other classes here once created.</todo>
     */
    public void getData( Constants.COLLECTION_NAME collectionName, String documentName, DatabaseListener listener,
                         DatabaseObject requestClassType ) {
        getData( userDocument.collection( collectionName.toString() )
                .document( documentName ), listener, requestClassType );
    }

    /**
     * Finds the data located in the given document and gives it to the listener to publish.
     * @param documentReference The {@link DocumentReference} which holds the reference to
     *                          the requested data.
     * @param listener The {@link Class} which impliments the {@link DatabaseListener}
     *      *                 and holds the listener for the success of data fetching.
     * @param requestClassType The {@link DatabaseObject} child class specifying which type of class
     *      *                         should be created and returned.
     */
    public void getData( DocumentReference documentReference,
                         DatabaseListener listener, DatabaseObject requestClassType ) {
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if ( task.isSuccessful() ) {
                            Log.d( GET_DATA_TAG, "Data has been found." );
                            //<todo> Will need change these once classes have been flushed out.
                            listener.onDataFetchSuccess(
                                    task.getResult().toObject( requestClassType.getClass() ) );
                        } else {
                            Log.d( GET_DATA_TAG, "Data could not be found." );
                        }
                    }
                });
    }

    /**
     * This will return a reference to a document which can be then used in other classes. The
     * intended purpose of this is to allow for references to be made attributes for specific
     * classes.
     * @param collectionName {@link Enum} of the collection name which the document is located.
     * @param data {@link DatabaseObject} object holding the data of the requested
     *                                   document name which is being requested.
     * @return {@link DocumentReference} referring to the requested document in the given path.
     */
    public DocumentReference getReferenceTo( Constants.COLLECTION_NAME collectionName, DatabaseObject data ) {
        return userDocument.collection( collectionName.toString() ).document( data.getName() );
    }

    // https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {

                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i( IP_TAG, "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e( IP_TAG, ex.toString());
        }
        return null;
    }

    /**
     * This performs the same function as addData(), created to create clarity on use.
     * @param collectionName The {@link Enum} of the collection which holds desired document.
     * @param documentName Document namme represented as a {@link String} which data is being
     *                     written too.
     * @param data {@link DatabaseObject} holding data to be stored in the document.
     */
    public void updateData( Constants.COLLECTION_NAME collectionName, String documentName, DatabaseObject data ) {
        addData( collectionName, documentName, data );
    }
}
