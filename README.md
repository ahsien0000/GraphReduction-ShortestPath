## Graph data reduction algorithm

This repo provides an algorithm for users to reduce grpah-based data. For a specific algorithm (i.e., shortest path problem), users can obtain shortest path query result after applying shortest path algorithm on the original graph. However, since the original graph might be considered too large, the provided reduction algorithm can reduce the oginal graph by removing some irrelevant nodes/edges, but without harming the query result. In other words, the reduced graph can still answer the user's shortest path probelm query with a 100% accurate result.

### GRSP

GRSP: graph reduction-shortest path is the algorithm that reduces irrelevant nodes/edges in the original graph, while preserving 100% accuracy of the shortest path query result.
Refer to [algorithms](https://github.com/ahsien0000/GraphReduction_ShortestPath/tree/master/src/algorithm).

### LGRSP

LGRSP 1+epsilon: Lossy graph reduction-shortest path. Based on GRSP, LGRSP is the algorithm that reduces more nodes/edges with a tolerable error rate. In addition, the error rate is bounded by epsilon.
Refer to [algorithms](https://github.com/ahsien0000/GraphReduction_ShortestPath/tree/master/src/algorithm).

### Publications referring these algorithms

1. Wang, S., Zhang, G., Sheu, P., Hayakawa, M., Shigematsu, H., & Kitazawa, A. (2018). A Semantic Approach to Data Reduction for Weighted Graphs and Complex Queries. International Journal of Semantic Computing, 12(02), 287-312. [link](https://www.worldscientific.com/doi/abs/10.1142/S1793351X18500010)

2. Wang, S., Zhang, G., Sheu, P., Hayakawa, M., Shigematsu, H., & Kitazawa, A. (2018). Lossy Graph Data Reduction. International Journal of Semantic Computing, 12(03), 425-456. [link](https://www.worldscientific.com/doi/abs/10.1142/S1793351X18500022)


### Support or contacts
[contact support](chunghc3@uci.edu), and we’ll help you sort it out.
