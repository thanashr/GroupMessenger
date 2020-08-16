# GroupMessenger
Developed a multicast messenger application that can send every user-entered message to all the AVDs in the group.

The messages were stored as key-value pairs in a file-based content provider.

Total and FIFO ordering were preserved in real time by implementing ISIS algorithm.

Failure of app instance at the middle of execution was handled by the decentralized property of the algorithm.

Hosted the website in AWS EC2 instance.
