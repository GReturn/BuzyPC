package io.buzypc.app.UI.Utils

import android.content.Context
import io.buzypc.app.Data.AppSession.BuzyUserAppSession
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.Data.SharedPrefManagers.BuzyUserBuildPrefManager
import io.buzypc.app.Data.SharedPrefManagers.BuzyUserManager
import io.buzypc.app.Data.SharedPrefManagers.SessionManager

/**
 * Loads the details of the currently logged-in user.
 *
 * This function retrieves the username from the application's session (BuzyUserAppSession)
 * and uses it to load the corresponding user details. It then returns a BuzyUser object
 * populated with this information.
 *
 * @param context The application context. This is used to access the application's session
 *                and for initializing the BuzyUser object.
 * @return A [BuzyUserManager] object containing the details of the currently logged-in user.
 *         The BuzyUser object will have its properties populated based on the user data
 *         associated with the retrieved username.
 * @throws IllegalStateException if the application context is not an instance of BuzyUserAppSession.
 *         This indicates an error in application setup.
 * @see BuzyUserManager
 * @see BuzyUserAppSession
 */
fun loadCurrentUserDetails(context: Context): BuzyUserManager {
    val userDetails = BuzyUserManager(context)
    userDetails.loadUser((context.applicationContext as BuzyUserAppSession).username)
    return userDetails
}