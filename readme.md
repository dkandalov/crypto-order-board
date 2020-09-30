See [task description](Crypto_v1.0.pdf)

Assumptions:
 - summary is done by sell/buy and coin type (i.e. orders are merged by price and coin type)
 - prices are always in pounds
 - single-threaded
 - doing the simplest possible thing (over performance and other considerations)

Some things to think about:
 - input validation
 - should coin type be constrained to a set of values or not?
 - user id, coin type format
 - it might be useful to have order ids
 - are quantity and price always above 0?
