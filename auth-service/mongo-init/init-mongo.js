db.createUser(
    {
        user : "oauth-user",
        pwd : "password",
        roles : [
          {
            role : "readWrite",
            db : "oauth-db",
          }
          ]

    })