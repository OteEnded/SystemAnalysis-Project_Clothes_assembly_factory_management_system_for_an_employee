package ku.cs.service;

import ku.cs.entity.Works;
import ku.cs.model.Work;
import ku.cs.utility.ProjectUtility;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkCalendar {

    private static List<HashMap<String, Object>> workCalendar;

    public static void init() throws SQLException {
        fetch();
    }

    public static List<HashMap<String, Object>> fetch() throws SQLException {
        return fetch(Works.load());
    }

    public static List<HashMap<String, Object>> fetch(HashMap<String, Work> workInPlan) throws SQLException {
        ProjectUtility.debug("WorkCalendar[fetch]: fetching...");
//        List<HashMap<String, Object>> fetchingWorkCalendar = new ArrayList<>();
        workCalendar = new ArrayList<>();

        List<String> notInPlan = new ArrayList<>();
        for (Work work: workInPlan.values()) {
            if (!isWorkPlanAble(work)) { notInPlan.add(Works.getJoinedPrimaryKeys(work)); }
        }
        ProjectUtility.debug("WorkCalendar[fetch]: notInPlan ->", notInPlan);
        for (String key: notInPlan) {
            workInPlan.remove(key);
        }


        if (workInPlan.isEmpty()) {
            ProjectUtility.debug("WorkCalendar[fetch]: workInPlan is empty");
            return null;
        }

        List<Work> sortedWorkInPlan = Works.getSortedBy("deadline", workInPlan);
        ProjectUtility.debug("WorkCalendar[fetch]: sortedWorkInPlan ->", sortedWorkInPlan);

        for (Work work: sortedWorkInPlan) {
            ProjectUtility.debug("WorkCalendar[fetch]: putting in work ->", work);
            HashMap<String, Object> event = new HashMap<>();
            event.put("work", work);
            event.put("start_date", getExpectedStartDate(work));
            event.put("done_date", getExpectedDoneDate(work));
            workCalendar.add(event);
        }

        ProjectUtility.debug("WorkCalendar[fetch]: fetching done");
        ProjectUtility.debug("WorkCalendar[fetch]: fetchingWorkCalendar ->", workCalendar);
        return workCalendar;
    }

    public static Date getExpectedStartDate(Work work) throws SQLException {
        if (work.getStartDate() != null) return work.getStartDate();
        ProjectUtility.debug("WorkCalendar[getExpectedStartDate]: workCalendar ->", workCalendar);
        ProjectUtility.debug("WorkCalendar[getExpectedStartDate]: work ->", work);
        if (workCalendar == null || workCalendar.isEmpty()) {
            if (work.getStatus().equals(Works.status_waitForAccept) || work.getStatus().equals(Works.status_waitForMaterial)) {
                ProjectUtility.debug("WorkCalendar[getExpectedStartDate]: work status is " + work.getStatus());
                ProjectUtility.debug("WorkCalendar[getExpectedStartDate]: return " + ProjectUtility.getDate(1));
                return ProjectUtility.getDate(1);
            }
            else if (work.getStatus().equals(Works.status_waitForWorking)) {
                work.setStartDate(ProjectUtility.getDate());
                work.setStatus(Works.status_working);
                return ProjectUtility.getDate();
            }
            throw new RuntimeException("WorkCalendar[getExpectedStartDate]: #### workCalendar is empty but work status is " + work.getStatus());
        }
        HashMap<String, Object> lastEvent = workCalendar.get(workCalendar.size() - 1);
        Date lastEventDoneDate = (Date) lastEvent.get("done_date");
        if (lastEventDoneDate == null) throw new RuntimeException("WorkCalendar[getExpectedStartDate]: #### lastEventDoneDate is null");
        return ProjectUtility.getDateWithOffset(lastEventDoneDate, 1);
    }

    public static Date getExpectedDoneDate(Work work) throws SQLException {
        if (workCalendar == null) init();
        ProjectUtility.debug("WorkCalendar[getExpectedDoneDate]: getting ExpectedDoneDate");
        if (getExpectedStartDate(work) == null) throw new RuntimeException("WorkCalendar[getExpectedDoneDate]: #### getExpectedStartDate(work) is null");
        return ProjectUtility.getDateWithOffset(getExpectedStartDate(work), work.getEstimatedWorkDay());

//        if (workCalendar.isEmpty()) return ProjectUtility.getDateWithOffset(getExpectedStartDate(work), work.getEstimatedWorkDay());
//        else {
//            HashMap<String, Object> lastEvent = workCalendar.get(workCalendar.size() - 1);
//            Date lastEventDoneDate = (Date) lastEvent.get("done_date");
//            if (lastEventDoneDate == null) throw new RuntimeException("WorkCalendar[getExpectedDoneDate]: #### lastEventDoneDate is null");
//            return ProjectUtility.getDateWithOffset(lastEventDoneDate, work.getEstimatedWorkDay() + 1);
//        }

    }

    public static HashMap<String, Object> getEventFromWork(Work work) throws SQLException {
        if (workCalendar == null) init();
        return getEventFromWork(work, workCalendar);
    }

    public static HashMap<String, Object> getEventFromWork(Work work, List<HashMap<String, Object>> workCalendar) throws SQLException {
        if (workCalendar == null) throw new RuntimeException("WorkCalendar[getEventFromWork]: #### workCalendar is null");

        for (HashMap<String, Object> event: workCalendar){
            Work workInEvent = (Work) event.get("work");
            if (workInEvent.getId().equals(work.getId())) return event;
        }

        return null;
    }

    private static boolean isNew(Work work) throws SQLException {
        if (workCalendar == null) init();
        return (getEventFromWork(work) == null);
    }

    public List<HashMap<String, Object>> getWorkCalendar() {
        return workCalendar;
    }

    public static String getWorkEstimating(Work work) throws SQLException {
        init();

        if (!isWorkPlanAble(work)) return "cannot estimate this work";

        HashMap<String, Work> workInPlan = new HashMap<>(Works.getData());
        if (work.getId() == null) work.setId(Works.getNewId());
        workInPlan.put(work.getId(), work);
        List<HashMap<String, Object>> tempWorkCalendar = fetch(workInPlan);
        HashMap<String, Object> workEvent = getEventFromWork(work, tempWorkCalendar);

        ProjectUtility.debug("WorkCalendar[getWorkEstimating]: tempWorkCalendar ->", tempWorkCalendar);
        ProjectUtility.debug("WorkCalendar[getWorkEstimating]: workEvent ->", workEvent);

        if (ProjectUtility.differanceDate(work.getDeadline(), (Date) workEvent.get("done_date")) < 0)
            return Works.estimate_late;
        else return Works.estimate_onTime;
    }

    public static boolean isWorkPlanAble(Work work) throws SQLException {
        return !(
                work.getStatus().equals(Works.status_done) ||
                work.getStatus().equals(Works.status_sent) ||
                work.getStatus().equals(Works.status_checked) ||
                work.getProductId() == null ||
                work.getProduct().getProgressRate() == -1
        );
    }

    public static void print(){
        print(workCalendar);
    }

    public static void print(List<HashMap<String, Object>> printingWorkCalendar){
        List<String> printStr = new ArrayList<>();
        if (printingWorkCalendar == null){
            printStr.add("WorkCalendar[print]: #### printingWorkCalendar is null");
            ProjectUtility.debug(printStr);
        }
    }



}
