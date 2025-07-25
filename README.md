# SCIM Stub Java app for testing Okta integration

## To get this to run

1. Install Java and set JAVA_HOME environment variable
2. Run in terminal: `mvn clean spring-boot:run`
3. Run `curl -u $SCIM_USER:$SCIM_PASSWORD http://localhost:8080/scim/v2/ServiceProviderConfig` - should show some JSON.

##Using Ngrok to expose API to internet
To test this locally Okta you need to expose your local API to the internet. You can do this using ngrok. Install that here: https://ngrok.com/download OR by doing these commands:

1. `curl -s https://ngrok-agent.s3.amazonaws.com/ngrok.asc | sudo tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null`
2. `echo "deb https://ngrok-agent.s3.amazonaws.com buster main" | sudo tee /etc/apt/sources.list.d/ngrok.list`
3. `sudo apt update && sudo apt install ngrok`

Then do the following:

1. Go to https://dashboard.ngrok.com/get-started/setup, login, copy your auth token
2. Run `ngrok config add-authtoken <paste-your-auth-token-here>`
3. While your Spring Boot applicaiton is still running, run `ngrok http 8080` in another tab.
4. You'll see some output like `Forwarding https://xxxxxxxxxxxx.ngrok-free.app -> http://localhost:8080`
5. Use that base URL, do `curl -u $SCIM_USER:$SCIM_PASSWORD https://xxxxxxxxxxxx.ngrok-free.app/scim/v2/ServiceProviderConfig`
6. The same JSON should appear.

## Now in Okta:

1. In Applications - > My SCIM 2.0 Test App (Header Auth) -> "Provisioning" tab, Click "Configure API integration" then "Enable API integration"
2. Set SCIM 2.0 Base Url = "https://xxxxxxxxxxxx.ngrok-free.app/scim/v2"
3. enter aN api key eg "my-super-secret-token"
4. Click "Test API credentials. if works will say "SCIM 2.0 Test App (Header Auth) was verified successfully!"
5. Click "Save"
6. Now in the provisioning tab, under "provisioning to app" click edit and check Create Users. Then save.

## Testing that creating a user pushes that user to your app

1. In Okta, Assignments tab of your SCIM app. Assign -> Assign to people
2. Choose a user eg yourself and click "Assign", then "Save and go back"
3. View the output of your Spring Boot app and you'll see that your app received a user from Okta.
