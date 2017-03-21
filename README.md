# Language Model 

POC implementation based on perfect hashing algorithms and fibonacci compression. In case of read-only implementation O(1) access could be achieved by performing O(n) preprocessing. In case of storing hash values instead of actual keys we can get constant performance and space with minor FP ratio. 

# Collision probability

In case of Int32 data types used for hash values, probability of collision would be:

    P(k) = 1 - exp(-k(k-1)*2^-33)
    
Where k is number of ngram keys. 
