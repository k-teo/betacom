### How to run project:
Change database connection properties according to your local MySQL database <br />

File:
* src/main/resources/application.properties <br />


Properties to change:
*  spring.datasource.url=
*  spring.datasource.username=
*  spring.datasource.password=


After it, run the application. You can use e.g. Postman to see how it works.

### Endpoints:

*  POST /login - authenticate user
*  POST /register - create new user (it has to have a unique login)
*  POST /items - get all item of logged-in user (requires bearer token)
*  GET /items - add new item for logged-in user (requires bearer token)


