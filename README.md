This is an e-commerce platform, which allows users to buy or sell products. Only authenticated users can add products. Authentication is available via endpoint "/login". User can rate each product by 5 stars rating system. Each product has reviews, added by other users. Each review can be rated by other users by giving thumbs up or down.
Product rating is counted by given formula: sum of all the stars / number of ratings.

Users have roles. There are three roles: user who can only view and add products, moderator who manages certain category of products and admin who can manage all products.

In a database there are three accounts created. That is user(with role user and password user), moderator(with role moderator and password moderator), goly(with role admin and password goly).
