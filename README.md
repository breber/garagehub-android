## Publishing an Update

* Increment the `versionCode` attribute in the AndroidManifest.xml
* If any of the models have changed
  * Increment the `android:value` attribute of the `<meta-data android:name="VERSION">` tag
  * Create a SQL upgrade script (http://satyan.github.io/sugar/migration.html)
* Update the `versionName` attribute to the next logical version (x.x.x convention)
* Export a signed APK from Eclipse using the key that is in the Dropbox folder
* Upload the new APK to the Google Play Developer Console
* Add what changed to the `Recent Changes` section on the Developer Console
* Copy the signed APK to the `Releases` folder in Dropbox
* Tag the release in git

        git tag v1.1.1
        git push --tags
