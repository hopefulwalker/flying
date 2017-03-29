Version = 0.2.0
1. Endpoint class overrides hashCode and equals. This simplify the implementation of setEndpoints in class ZMQUCClientEngine and PooledZMQClientEngineFactory.(3)
2. Fixed the bug to get the ip4 address of local host, add class variables to store local ip4 as string and integer.(1)
3. Improved the build.gradle file of root project, just for easy to migrate to develop in linux.(1) Developer should add WORK_HOME
