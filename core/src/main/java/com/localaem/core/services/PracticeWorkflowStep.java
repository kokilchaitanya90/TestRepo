package com.localaem.core.services;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@Component(service = WorkflowProcess.class,property={
        "process.label"+"=Practice AEM Workflow Process Step",
        Constants.SERVICE_VENDOR+"=AEM PRACTICE",
        Constants.SERVICE_DESCRIPTION+"= Custom aem practice workflow process step"
})
public class PracticeWorkflowStep implements WorkflowProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(PracticeWorkflowStep.class);

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        try {
            WorkflowData workflowData = workItem.getWorkflowData();
            if(workflowData.getPayloadType().equalsIgnoreCase("JCR_PATH")){
                Session session = workflowSession.adaptTo(Session.class);
                String path = workflowData.getPayload().toString()+"/jcr:content";
                Node node = (Node) session.getItem(path);
                String[] processArgs = metaDataMap.get("PROCESS_ARGS","string").toString().split(",");
                for(String wfArgs : processArgs){
                    String[] args = wfArgs.split(":");
                    if(node != null){
                        node.setProperty(args[0],args[1]);
                    }
                }
            }
        } catch (RepositoryException e) {
            LOGGER.error("PracticeWorkflowStep :: Exception : {} ",e);
        }
    }
}
