# Desktop
> Desktop platform using JavaFX

## Requirements
- Backend server set up and running. Please refer to corresponding [documentation](../backend/README.md)
- Backend connected to the database successfully
- IntelliJ IDEA
- JavaFX SDK
- JDK version 15

## Run

```bash
git clone https://github.com/KevinZiadeh/Exchange-Rate-App.git
```
- Open the `Exchange-Rate-App/desktop` folder using IntelliJ IDEA. 
- Wait for Grade to finish building. This may take a few minutes.
- In your IDE, open the Project Structure dialog. (`File` -> `Project Structure`). 
- On the left select `libraries`. 
- Press `Add`, and select `From Maven`. 
- Type in `com.squareup.retrofit2:retrofit:2.9.0`, and make sure to select `Transitive Dependencies`,
`JavaDocs`, and `Annotations`. The download destination should be the lib folder within your project directory.
- Upon completion, repeat the above step, but for the following package: `com.squareup.retrofit2:converter-gson:2.9.0`.
- Run the application by pressing on the Run button.

## Overview
### Structure
<p align="center">
  <img src="../res/desktop_structure.PNG?raw=true"/>
</p>

### API
The API package contains all the needed information and code to be able to communicate with the backend.
If the backend is being run on another device, you need to edit the value of `API_URL` in `/api/ExchangeService.java`  to match the URL and PORT of the backend server.

#### Model
The `model` package inside `api` package contains all the needed information about all the models that are used when interfacing with the backend.

### UI
The UI package contains all the information used to display our application and interface with it, be it logging in, conditional renderign depending on if user is logged in or not, fields and inputs used by the user, etc...

The UI is divided into a `parent.fxml` file and other directories. Each directory consists of an fxml and a css file which can be accessed by using the side menu.
 
 
