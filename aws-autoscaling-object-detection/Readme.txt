Project Members:
• Darshan Dagly (ddagly@asu.edu)
• Smruti Berad (sberad@asu.edu)
• Viken Shaumitra Parikh (vparikh2@asu.edu)
Public IP: http://*:9000/

AWS Credentials:
Access Key - *
Secret Key - *
URL - http://*:9000
S3 Bucket Name : *


1. Project Description
The aim of the project is to build an elastic application using Amazon Web Services. 
The application will provide a video surveillance service to its users. 
It will handle all the incoming requests made by scaling out and then scale in when not in demand. 
The following services from AWS were made use of:
• Elastic Compute Cloud
• Simple Queue Service
• Simple Storage Service

2. Web Server
This takes in requests from the user and send it to the request queue. 
It then picks up the response from another queue and displays the video name and the objects recognised separated by commas.

3. App Server
This takes a request from the request queue placed by the web server, performs deep learning and puts the result in the S3 bucket. 
It then puts the result into the response queue which is then used by the web server.