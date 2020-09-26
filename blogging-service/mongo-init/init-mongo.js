db.createUser(
    {
        user : "blogging-user",
        pwd : "password",
        roles : [
          {
            role : "readWrite",
            db : "blogging",
          }
          ]

    })