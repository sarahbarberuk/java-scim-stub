# SCIM Stub Java app for testing Okta integration

## To get this to run

1. Install Java and set JAVA_HOME environment variable
2. Create an .env file from .env.example and fill in some credentials
3. Run in terminal: `source .env`
4. Run in terminal: `mvn clean spring-boot:run`
5. Run `curl -u $SCIM_USER:$SCIM_PASSWORD http://localhost:8080/scim/v2/ServiceProviderConfig` - should show some JSON.

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
