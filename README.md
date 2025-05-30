# mobile-app-csv-upload-select-criteria-view-as-list

This mobile application was created using Android Studio and it's a collaboration between me and my good friends and collagues Michalis Anagnostopoulos and Andreas Parros.

**The application's functionalities are as follows:**

A. Home screen with the ability to upload a file from the local disk or via URL.

B. File parsing based on the structure of the program file as provided in the link (In Latin characters but Greek language in the given .csv):
https://www.dind.uoa.gr/fileadmin/depts/dind.uoa.gr/www/uploads/Orologio_DinD_CHeimerino_2023_R4.pdf

C. Creation of database records using RetroFit DB

D. Creation of a screen for displaying data based on criteria (including a list of criteria, a submit button, and changing the database query depending on the selection).

E. Screen/activity for displaying database data on the mobile screen, with the ability to return to the previous screen.

**Tools & Applications used to create the present Mobile App:**
- Android Studio
- DBeaver
- Node.js
- MySQL

**File Clarifications:**
- Android Studio Project Name: ProjectPart1Attempt1
- Server.js Code: https://drive.google.com/file/d/1pCgewiR9Vz-OF89cpvBvFhJEllOO5ofy/view?usp=sharing
- .csv File: https://gist.github.com/athalamprou/5f34aa7f00639596484707185c01fc29 (you can download the file)

**Clarifications:**
- The comments are in Greeklish (greek in latin characters)
- baseURL in Retrofit is configured to use localhost in port 5000, you can change it to your desired IP and port in the same format, just be sure to upload the necessary server.js file
