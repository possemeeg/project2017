package com.possemeeg.project2017.webdoor.component;

@Component
public class ProcessContext implements DataSerializable {
    private final long processId;
    @Autowired
    public ProcessContext(HazelcastInstance hazelcastInstance) {
        hazelcastInstance.getReliableIdGenerator(PROCESS_ID_GENERATOR).newId();
    }
    private ProcessContext() {
    }
    public long getProcessId() {
        return processId;
    }
    public static ProcessContext fromObjectDataInput(ObjectDataInput in) throws IOException {
        ProcessContext newContext = new ProcessContext();
        newContext.readData(in);
        return newContext;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(processId);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        processId = in.readLong();
    }
}

