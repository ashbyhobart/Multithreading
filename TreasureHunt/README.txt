Program developed to determine the position of a "treasure" as outlined 
below, using multithreading to be more efficient. This project was part 
of an assignment given by Professors Azer Bestavros and Renato Mancuso of 
Boston University as part of the CS 350 course curriculum.

"The treasure map is a graph of hashed hints towards a treasure inside an input
file. You are given a file name and a starting offset K0 inside the file. At the specified offset, you will
find two valid MD5 hashes one after the other (without any gap) to crack using the code developed
in the previous assignment, and the cycle repeats. Each of these two hashes will reveal either a new
offset or an operation. If it is an offset, say (K1,1), it means a pair of hashes are located at K1,1 bytes
from the beginning of the file, if it an operation, we act as follows:
• If it is an ‘add’ operation and this operation is next to k1,1, it means a pair of hashes are located
at (k1,1 + k1,1) bytes from the beginning of the file.
• If it is a ‘mul’ operation, and this operation is next to k1,1, it means a pair of hashes are located
at (k1,1 × 3) bytes from the beginning of the file.
• If it is a ‘div’ operation, and this operation is next to k1,1, it means a pair of hashes are located
at (k1,1 ÷ 3) bytes from the beginning of the file.
Some of the hashes, however, can encode for previously decoded offsets, effectively pointing
backwards to a previously visited node. If a cracked hash is not a valid offset (i.e., it is beyond the file
length), there could be two cases. If said offset is an even number, it is a boot and it can be discarded.
If it is an odd number, then you have found the treasure! There is always one and only one treasure
to be found."