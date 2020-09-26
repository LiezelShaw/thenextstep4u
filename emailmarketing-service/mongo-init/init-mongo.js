db.createUser(
    {
        user : "email-user",
        pwd : "password",
        roles : [
          {
            role : "readWrite",
            db : "email-marketing",
          }
          ]

    })