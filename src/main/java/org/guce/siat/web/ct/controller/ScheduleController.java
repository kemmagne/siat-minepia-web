package org.guce.siat.web.ct.controller;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.guce.siat.common.model.Appointment;
import org.guce.siat.common.model.Car;
import org.guce.siat.common.model.Holiday;
import org.guce.siat.common.model.HourlyType;
import org.guce.siat.common.model.InspectionWorkDayConfig;
import org.guce.siat.common.model.InspectionWorkWeekConfig;
import org.guce.siat.common.model.Service;
import org.guce.siat.common.model.User;
import org.guce.siat.common.model.WorkYearConfig;
import org.guce.siat.common.service.AppointmentService;
import org.guce.siat.common.service.CarService;
import org.guce.siat.common.service.ControllerHolidayService;
import org.guce.siat.common.service.HolidayService;
import org.guce.siat.common.service.InspectionWorkDayConfigService;
import org.guce.siat.common.service.InspectionWorkWeekConfigService;
import org.guce.siat.common.service.UserService;
import org.guce.siat.common.service.WorkYearConfigService;
import org.guce.siat.common.utils.Constants;
import org.guce.siat.common.utils.DateUtils;
import org.guce.siat.web.common.ControllerConstants;
import org.guce.siat.web.common.util.AppointmentEvent;
import org.guce.siat.web.common.util.AppointmentModel;
import org.guce.siat.web.ct.controller.util.InspectorCheck;
import org.guce.siat.web.ct.controller.util.JsfUtil;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ScheduleController.
 */
@ManagedBean
@ViewScoped
public class ScheduleController implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleController.class);

    /**
     * The Constant LOCAL_BUNDLE_NAME.
     */
    private static final String LOCAL_BUNDLE_NAME = "org.guce.siat.messages.locale";

    /**
     * The Constant CALENDAR_EVENT_TYPE.
     */
    private static final String CALENDAR_EVENT_TYPE = "Calendar_eventType_";

    /**
     * The Constant MISSING_HOURLY_TYPE_CONFIG.
     */
    private static final String MISSING_INSPECTION_WORK_WEEK_CONFIG = "missing_inspection_work_week_config";

    /**
     * The Constant MISSING_INSPECTION_WORK_DAY_CONFIG.
     */
    private static final String MISSING_INSPECTION_WORK_DAY_CONFIG = "missing_inspection_work_day_config";

    /**
     * The Constant MISSING_CAR.
     */
    private static final String MISSING_CAR = "missing_car";

    /**
     * The Constant CALENDAR_CAR.
     */
    private static final String CALENDAR_CAR = "Calendar_car";

    /**
     * The Constant DEFAULT_DURATION_INSPECTION.
     */
    // In hours
    private static final Integer DEFAULT_DURATION_INSPECTION = 2;

    /**
     * The Constant EVENT_PROPOSED.
     */
    private static final String EVENT_PROPOSED = "proposed";

    /**
     * The Constant EVENT_OFF.
     */
    private static final String EVENT_OFF = "off";

    /**
     * The Constant EVENT_BUSY.
     */
    private static final String EVENT_BUSY = "busy";

    /**
     * The Constant EVENT_HOLIDAY.
     */
    private static final String EVENT_HOLIDAY = "holiday";

    /**
     * The Constant EVENT_NEW.
     */
    private static final String EVENT_NEW = "new";

    /**
     * The appointment service.
     */
    @ManagedProperty(value = "#{appointmentService}")
    private AppointmentService appointmentService;

    /**
     * The inspection work week config service.
     */
    @ManagedProperty(value = "#{inspectionWorkWeekConfigService}")
    private InspectionWorkWeekConfigService inspectionWorkWeekConfigService;

    /**
     * The inspection work day config service.
     */
    @ManagedProperty(value = "#{inspectionWorkDayConfigService}")
    private InspectionWorkDayConfigService inspectionWorkDayConfigService;

    /**
     * The work year config service.
     */
    @ManagedProperty(value = "#{workYearConfigService}")
    private WorkYearConfigService workYearConfigService;

    /**
     * The holiday service.
     */
    @ManagedProperty(value = "#{holidayService}")
    private HolidayService holidayService;

    /**
     * The user service.
     */
    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    /**
     * The inspector holiday service.
     */
    @ManagedProperty(value = "#{controllerHolidayService}")
    private ControllerHolidayService controllerHolidayService;

    /**
     * The car service.
     */
    @ManagedProperty(value = "#{carService}")
    private CarService carService;

    /**
     * The appointment list.
     */
    private List<Appointment> appointmentList;

    /**
     * The car list.
     */
    private List<Car> carList;

    /**
     * The car available list.
     */
    private List<Car> carAvailableList;

    /**
     * The inspection work day configs list.
     */
    private List<InspectionWorkDayConfig> inspectionWorkDayConfigsList;

    /**
     * The holiday list.
     */
    private List<Holiday> holidayList;

    /**
     * The inspector list.
     */
    List<User> inspectorList;

    /**
     * The inspector check list.
     */
    List<InspectorCheck> inspectorCheckList;

    /**
     * The proposed events.
     */
    private List<AppointmentEvent> proposedEvents;

    /**
     * The calendar start date.
     */
    private Date calendarStartDate;

    /**
     * The calendar endt date.
     */
    private Date calendarEndtDate;

    /**
     * The current year.
     */
    private Integer currentYear;

    /**
     * The work year config list of current year.
     */
    private List<WorkYearConfig> workYearConfigListOfCurrentYear;

    /**
     * The work year config list.
     */
    private List<WorkYearConfig> workYearConfigList;

    /**
     * The inspection work week config list.
     */
    private List<InspectionWorkWeekConfig> inspectionWorkWeekConfigList;

    /**
     * The current service.
     */
    protected Service currentService;

    /**
     * The event model.
     */
    private ScheduleModel eventModel;

    /**
     * The event.
     */
    private AppointmentEvent event;

    /**
     * The added event.
     */
    private AppointmentEvent addedEvent;

    /**
     * The selected event.
     */
    private AppointmentEvent selectedEvent;

    /**
     * The deleted event.
     */
    private boolean deletedEvent;

    /**
     * The rdv confirmation.
     */
    private boolean rdvConfirmation;

    /**
     * Inits the.
     */
    public void init() {
        event = null;
        deletedEvent = Boolean.FALSE;
        addedEvent = null;
        inspectorList = userService.findControleursByService(getCurrentService());
        inspectorCheckList = convertInspectorListToInspectorCheckList(inspectorList);
        appointmentList = appointmentService.findAppointmentsByControllerList(inspectorList);
        inspectionWorkWeekConfigList = inspectionWorkWeekConfigService.findIWWConfigByOrganism(getCurrentService()
                .getSubDepartment().getOrganism());
        carList = carService.findByOrganism(getCurrentService().getSubDepartment().getOrganism());
        eventModel = new AppointmentModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void loadEvents(final Date start, final Date end) {
                clear();
                calendarStartDate = start;
                calendarEndtDate = end;
                try {
                    calculAndGenerationCalendar();
                } catch (final ParseException e) {
                    LOG.error(e.getMessage(), e);
                }
            }

        };

    }

    /**
     * Calcul and generation calendar.
     *
     * @throws ParseException the parse exception
     */
    public void calculAndGenerationCalendar() throws ParseException {

        holidayList = holidayService.findBetweenTwoDates(calendarStartDate, calendarEndtDate);
        // division of the period between start date et end date of calendar by workYearConfig
        divideDatesByWorkYearConfig();
        if (eventModel.getEvents() != null) {
            eventModel.getEvents().clear();
        }
        // add holidays to calendar
        fillHolidays();

        // add proposed events for the calendar
        addEventsFromInspectionWorkDayConfig();

        // add appointments already affected to the calendar
        addAppointmentToSchedule();
        addCreatedEventForInspection();

        if (selectedEvent != null && deletedEvent) {
            removeAppointment(selectedEvent);
        }
        deletedEvent = false;
        //delete appointment proposition if his controller not checked
        if (isRdvConfirmation()) {
            final User controller = event.getAppointment().getController();
            @SuppressWarnings("unchecked")
            final List<InspectorCheck> checkedControllers = (List<InspectorCheck>) CollectionUtils.select(inspectorCheckList,
                    new Predicate() {
                @Override
                public boolean evaluate(final Object object) {
                    final InspectorCheck check = (InspectorCheck) object;
                    return check.getController().getId().equals(controller.getId()) && check.getChecked();
                }
            });
            @SuppressWarnings("unchecked")
            final List<InspectorCheck> allControllers = (List<InspectorCheck>) CollectionUtils.select(inspectorCheckList,
                    new Predicate() {
                @Override
                public boolean evaluate(final Object object) {
                    final InspectorCheck check = (InspectorCheck) object;
                    return check.getChecked();
                }
            });
            if (CollectionUtils.isEmpty(checkedControllers) && CollectionUtils.isNotEmpty(allControllers)) {
                removeAppointment(event);
            }

        }

    }

    /**
     * Adds the created event for inspection.
     */
    private void addCreatedEventForInspection() {
        if (addedEvent != null) {
            eventModel.addEvent(addedEvent);
        }

    }

    /**
     * Creates the new event.
     */
    public void createNewEvent() {

        if (addedEvent != null) {
            eventModel.getEvents().remove(addedEvent);
        }
        if (event != null) {
            event.setStartDate(DateUtils.setHourAndMinutes(event.getStartDate(), event.getBeginHour(), event.getBeginMinutes()));
            event.getAppointment().setBeginTime(DateUtils.setHourAndMinutes(event.getStartDate(), event.getBeginHour(), event.getBeginMinutes()));
            event.setEndDate(DateUtils.addHours(event.getStartDate(), DEFAULT_DURATION_INSPECTION));
            event.setEditable(true);
            event.setController(event.getAppointment().getController());
            if (EVENT_NEW.equals(event.getTypeEvent())) {
                final String title = generateTitleForEvent(event.getTypeEvent(), event.getAppointment().getController(), null,
                        event.getAppointment());
                event.setTitle(title);
                event.setDescription(title);
                removeAppointment(event);
                eventModel.addEvent(event);
            } else {
                final String title = generateTitleForEvent(event.getTypeEvent(), event.getAppointment().getController(), null,
                        event.getAppointment());
                event.setTypeEvent(EVENT_NEW);
                event.setStyleClass(EVENT_NEW);
                event.setTitle(title);
                event.setDescription(title);
            }
            deletedEvent = true;
            addedEvent = event;
        }
    }

    /**
     * Creates the new event from apoitment.
     *
     * @param appointment the appointment
     */
    public void createNewEventFromApoitment(final Appointment appointment) {

        event = new AppointmentEvent();
        event.setAppointment(appointment);
        final Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(appointment.getBeginTime());
        event.setStartDate(DateUtils.setHourAndMinutes(beginCalendar.getTime(), beginCalendar.get(Calendar.HOUR),
                beginCalendar.get(Calendar.MINUTE)));
        event.setEndDate(DateUtils.addHours(event.getStartDate(), DEFAULT_DURATION_INSPECTION));
        event.setBeginHour(DateUtils.getHourFromDate(appointment.getBeginTime()));
        event.setBeginMinutes(DateUtils.getMinutesFromDate(appointment.getBeginTime()));
        event.setEditable(true);
        event.setController(appointment.getController());

        event.setStyleClass(EVENT_NEW);
        event.setTypeEvent(EVENT_NEW);
        final String title = generateTitleForEvent(event.getTypeEvent(), event.getAppointment().getController(), null,
                event.getAppointment());
        event.setTitle(title);
        event.setDescription(title);
        this.setSelectedEvent(event);
        removeAppointment(event);
        eventModel.addEvent(event);

        addedEvent = event;
        rdvConfirmation = true;
    }

    /**
     * Check date from inspection work week config.
     *
     * @param date the date
     * @param hourlyType the hourly type
     * @return the boolean
     */
    private Boolean checkDateFromInspectionWorkWeekConfig(final Date date, final HourlyType hourlyType) {
        final int dayOfWeek = DateUtils.dayOfWeek(date);
        final InspectionWorkWeekConfig currentInspectionWorkWeekConfig = getInspectionWorkWeekConfigByHourlyType(hourlyType);

        if (currentInspectionWorkWeekConfig != null) {
            if (dayOfWeek == Constants.ONE && currentInspectionWorkWeekConfig.getSunday()) {
                return true;
            }
            if (dayOfWeek == Constants.TWO && currentInspectionWorkWeekConfig.getMonday()) {
                return true;
            }

            if (dayOfWeek == Constants.THREE && currentInspectionWorkWeekConfig.getTuesday()) {
                return true;
            }

            if (dayOfWeek == Constants.FOUR && currentInspectionWorkWeekConfig.getWednesday()) {
                return true;
            }

            if (dayOfWeek == Constants.FIVE && currentInspectionWorkWeekConfig.getThursday()) {
                return true;
            }

            if (dayOfWeek == Constants.SIX && currentInspectionWorkWeekConfig.getFriday()) {
                return true;
            }

            if (dayOfWeek == Constants.SEVEN && currentInspectionWorkWeekConfig.getSaturday()) {
                return true;
            }

            return false;
        }

        return null;
    }

    /**
     * Gets the inspection work week config by hourly type.
     *
     * @param hourlyType the hourly type
     * @return the inspection work week config by hourly type
     */
    public InspectionWorkWeekConfig getInspectionWorkWeekConfigByHourlyType(final HourlyType hourlyType) {
        if (CollectionUtils.isNotEmpty(inspectionWorkWeekConfigList)) {
            for (final InspectionWorkWeekConfig workWeekConfig : inspectionWorkWeekConfigList) {
                if (workWeekConfig.getHourlyTypeId().getId().equals(hourlyType.getId())) {
                    return workWeekConfig;
                }

            }
        }
        return null;
    }

    /**
     * Adds the events from inspection work day config.
     *
     * @throws ParseException the parse exception
     */
    private void addEventsFromInspectionWorkDayConfig() throws ParseException {
        proposedEvents = new ArrayList<>();

        // start date of proposition should be after the current day
        final Date startDateForPropositions = getStartDateForProposedEvents();

        // if start date is null, the calendar refer to date before current date
        if (startDateForPropositions != null) {
            final Integer diffDates = DateUtils.getDifferenceIndays(calendarEndtDate.getTime(), startDateForPropositions.getTime());
            // i=0 is the start date for proposed events, diffDates the date for proposed events
            for (int i = 0; i < diffDates; i++) {
                final Date dateIncrement = DateUtils.addDays(startDateForPropositions, i);
                // retrieve the hourly type for the current day to check the hours for inspections
                final HourlyType hourlyType = getHourlyTypeFromDate(startDateForPropositions);

                // the day should'nt be a holiday and it's valid for inspection (check InspectionWorkWeekConfig)
                final Boolean isFormInspectionWorkWeekConfig = checkDateFromInspectionWorkWeekConfig(dateIncrement, hourlyType);

                if (isFormInspectionWorkWeekConfig == null) {
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                            getCurrentLocale()).getString(MISSING_INSPECTION_WORK_WEEK_CONFIG));
                    return;
                }

                if (isFormInspectionWorkWeekConfig && !checkHolidayDate(dateIncrement)) {
                    // list of inspector selected
                    for (final InspectorCheck inspectorCheck : inspectorCheckList) {
                        // inspector should be  checked to generate a proposed event
                        if (inspectorCheck.getChecked()) {
                            //check  inspector's inspector day off
                            final boolean checkDayOffForInspector = !Objects.equals(
                                    controllerHolidayService.findHolidayByControllerAndDay(inspectorCheck.getController(), dateIncrement),
                                    null);
                            if (checkDayOffForInspector) {
                                final AppointmentEvent scheduleEvent = new AppointmentEvent();

                                scheduleEvent.setStartDate(new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS).parse(DateUtils
                                        .formatSimpleDate(dateIncrement) + " 00:00:00"));
                                scheduleEvent.setEndDate(new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS).parse(DateUtils
                                        .formatSimpleDate(dateIncrement) + " 23:59:59"));
                                scheduleEvent.setStyleClass(EVENT_OFF);
                                scheduleEvent.setBeginHour((short) 0);
                                scheduleEvent.setBeginMinutes(0);
                                scheduleEvent.setTypeEvent(EVENT_OFF);
                                scheduleEvent.setEditable(false);
                                scheduleEvent.setController(inspectorCheck.getController());

                                scheduleEvent.setAppointment(null);
                                final String title = generateTitleForEvent(EVENT_OFF, inspectorCheck.getController(), null, null);
                                scheduleEvent.setTitle(title);
                                scheduleEvent.setDescription(title);
                                eventModel.addEvent(scheduleEvent);

                            } else // inspector is avalable for inspection
                            {

                                // the hours for inspection
                                final List<InspectionWorkDayConfig> inspectionWorkDayConfigs = inspectionWorkDayConfigService
                                        .findInspectionWorkDayConfigByHourlyTypeAndOrganism(hourlyType, getCurrentService()
                                                .getSubDepartment().getOrganism());

                                if (CollectionUtils.isEmpty(inspectionWorkDayConfigs)) {
                                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                                            getCurrentLocale()).getString(MISSING_INSPECTION_WORK_DAY_CONFIG));
                                    return;
                                }

                                for (final InspectionWorkDayConfig inspectionWorkDayConfig : inspectionWorkDayConfigs) {
                                    final Boolean availableCar = availableCar(dateIncrement, inspectionWorkDayConfig.getHour());

                                    if (BooleanUtils.isFalse(availableCar)) {
                                        JsfUtil.addSuccessMessage(ResourceBundle.getBundle(ControllerConstants.Bundle.LOCAL_BUNDLE_NAME,
                                                getCurrentLocale()).getString(MISSING_CAR));
                                        return;
                                    }

                                    // inspector shouldn't have an inspection in the same time of proposed event and there is an available car
                                    if (!checkInspectorHasAppointmentInSameTime(inspectorCheck.getController(), dateIncrement,
                                            inspectionWorkDayConfig.getHour())
                                            && availableCar
                                            && !checkNewPropositionInSameTime(inspectorCheck.getController(), dateIncrement,
                                                    inspectionWorkDayConfig.getHour())) {

                                        final String title = generateTitleForEvent(EVENT_PROPOSED, inspectorCheck.getController(), null,
                                                null);
                                        final AppointmentEvent scheduleEvent = new AppointmentEvent();
                                        scheduleEvent.setId(JsfUtil.generateRandomString());
                                        scheduleEvent.setTitle(title);
                                        scheduleEvent.setDescription(title);
                                        scheduleEvent.setStartDate(DateUtils.setHour(dateIncrement, inspectionWorkDayConfig.getHour()));
                                        scheduleEvent.setEndDate(DateUtils.setHour(dateIncrement, inspectionWorkDayConfig.getHour()
                                                + DEFAULT_DURATION_INSPECTION));
                                        scheduleEvent.setStyleClass(EVENT_PROPOSED);
                                        scheduleEvent.setTypeEvent(EVENT_PROPOSED);
                                        scheduleEvent.setBeginHour(inspectionWorkDayConfig.getHour());
                                        scheduleEvent.setBeginMinutes(0);
                                        scheduleEvent.setEditable(true);
                                        scheduleEvent.setController(inspectorCheck.getController());

                                        final Appointment proposedAppointment = new Appointment();
                                        proposedAppointment.setBeginTime(scheduleEvent.getStartDate());
                                        proposedAppointment.setEndTime(scheduleEvent.getEndDate());
                                        proposedAppointment.setController(inspectorCheck.getController());
                                        proposedAppointment.setHistory((short) 0);
                                        proposedAppointment.setInspectionOnDock(false);
                                        scheduleEvent.setAppointment(proposedAppointment);
                                        proposedEvents.add(scheduleEvent);
                                    }
                                }
                            }
                        }

                    }

                }

            }
        }

        eventModel.getEvents().addAll(proposedEvents);
        if (selectedEvent != null) {
            removeAppointment(selectedEvent);
        }
    }

    /**
     * Check new proposition in same time.
     *
     * @param inspector the inspector
     * @param dateIncrement the date increment
     * @param hour the hour
     * @return true, if successful
     * @throws ParseException the parse exception
     */
    private boolean checkNewPropositionInSameTime(final User inspector, final Date dateIncrement, final Short hour)
            throws ParseException {
        boolean foundNewProposition = false;
        if (addedEvent != null) {

            final String dateIncrementSample = DateUtils.formatSimpleDate(dateIncrement);
            final Date dateBeginIncrementWithHour = new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH).parse(dateIncrementSample
                    + " " + hour);
            final Date dateEndIncrementWithHour = new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH).parse(dateIncrementSample
                    + " " + (hour + DEFAULT_DURATION_INSPECTION));

            final String dateBeginAppointmentSample = DateUtils.formatSimpleDate(addedEvent.getStartDate());
            final String dateEndAppointmentSample = DateUtils.formatSimpleDate(addedEvent.getEndDate());

            // Conditions declared separately to avoid the complexity of the code (sonar error)
            final boolean condition1 = addedEvent.getController().getId().equals(inspector.getId())
                    && dateIncrementSample.equals(dateBeginAppointmentSample) && dateIncrementSample.equals(dateEndAppointmentSample);

            final boolean subCondition21 = dateBeginIncrementWithHour.after(addedEvent.getStartDate())
                    && dateBeginIncrementWithHour.before(addedEvent.getEndDate());
            final boolean subCondition22 = dateEndIncrementWithHour.after(addedEvent.getStartDate())
                    && dateEndIncrementWithHour.before(addedEvent.getEndDate());
            final boolean subCondition23 = addedEvent.getStartDate().after(dateBeginIncrementWithHour)
                    && addedEvent.getEndDate().before(dateEndIncrementWithHour);

            final boolean condtion2 = subCondition21 || subCondition22 || subCondition23;

            if (condition1 && condtion2) {
                foundNewProposition = true;
            }

        }
        return foundNewProposition;
    }

    /**
     * Generate title for event.
     *
     * @param eventType the event type
     * @param inspector the inspector
     * @param holiday the holiday
     * @param appointment the appointment
     * @return the string
     */
    private String generateTitleForEvent(final String eventType, final User inspector, final Holiday holiday,
            final Appointment appointment) {
        final StringBuilder stringBuilder = new StringBuilder();
        final String typeEvent = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(
                CALENDAR_EVENT_TYPE + eventType);

        if (EVENT_OFF.equals(eventType)) {
            stringBuilder.append(typeEvent).append(inspector.getFirstName()).append(" ").append(inspector.getLastName());
        }

        if (EVENT_HOLIDAY.equals(eventType)) {

            stringBuilder.append(typeEvent).append(holiday.getLabel());
        }

        if (EVENT_PROPOSED.equals(eventType)) {
            stringBuilder.append(typeEvent).append(inspector.getFirstName()).append(" ").append(inspector.getLastName());
        }
        if (EVENT_BUSY.equals(eventType) || EVENT_NEW.equals(eventType)) {

            final String carString = ResourceBundle.getBundle(LOCAL_BUNDLE_NAME, getCurrentLocale()).getString(CALENDAR_CAR);

            stringBuilder.append(typeEvent).append("\n").append(inspector.getFirstName()).append(" ")
                    .append(inspector.getLastName()).append("\n").append(carString).append(": ");

            if (appointment != null) {
                stringBuilder.append(appointment.getCar().getModel());
            }

        }

        return stringBuilder.toString();
    }

    /**
     * Available car.
     *
     * @param dateIncrement the date increment
     * @param hour the hour
     * @return true, if successful
     * @throws ParseException the parse exception
     */
    private boolean availableCar(final Date dateIncrement, final Short hour) throws ParseException {
        boolean carAvailable = false;
        if (carList != null && !carList.isEmpty()) {
            int numberAvailableCars = carList.size();
            final String dateIncrementSample = DateUtils.formatSimpleDate(dateIncrement);
            final Date dateBeginIncrementWithHour = new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH).parse(dateIncrementSample
                    + " " + hour);
            final Date dateEndIncrementWithHour = new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH).parse(dateIncrementSample
                    + " " + (hour + DEFAULT_DURATION_INSPECTION));

            for (final Appointment appointment : appointmentList) {
                final String dateBeginAppointmentSample = DateUtils.formatSimpleDate(appointment.getBeginTime());
                final String dateEndAppointmentSample = DateUtils.formatSimpleDate(appointment.getEndTime());
                final boolean condition1 = dateIncrementSample.equals(dateBeginAppointmentSample)
                        && dateIncrementSample.equals(dateEndAppointmentSample) && appointment.getCar() != null;

                final boolean condition2 = dateBeginIncrementWithHour.after(appointment.getBeginTime())
                        && dateBeginIncrementWithHour.before(appointment.getEndTime());
                final boolean condition3 = dateEndIncrementWithHour.after(appointment.getBeginTime())
                        && dateEndIncrementWithHour.before(appointment.getEndTime());
                final boolean condition4 = appointment.getBeginTime().after(dateBeginIncrementWithHour)
                        && appointment.getEndTime().before(dateEndIncrementWithHour);

                if (condition1 && (condition2 || condition3 || condition4)) {
                    numberAvailableCars--;
                }

            }

            if (numberAvailableCars > 0) {
                carAvailable = true;
            }

        }

        return carAvailable;
    }

    /**
     * Check inspector has appointment in same time.
     *
     * @param inspector the inspector
     * @param dateIncrement the date increment
     * @param hour the hour
     * @return true, if successful
     * @throws ParseException the parse exception
     */
    private boolean checkInspectorHasAppointmentInSameTime(final User inspector, final Date dateIncrement, final Short hour)
            throws ParseException {
        boolean hasAppointment = false;
        final String dateIncrementSample = DateUtils.formatSimpleDate(dateIncrement);
        final Date dateBeginIncrementWithHour = new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH).parse(dateIncrementSample
                + " " + hour);
        final Date dateEndIncrementWithHour = new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH).parse(dateIncrementSample + " "
                + (hour + DEFAULT_DURATION_INSPECTION));

        for (final Appointment appointment : appointmentList) {
            final String dateBeginAppointmentSample = DateUtils.formatSimpleDate(appointment.getBeginTime());
            final String dateEndAppointmentSample = DateUtils.formatSimpleDate(appointment.getEndTime());

            final boolean condition1 = appointment.getController().getId().equals(inspector.getId())
                    && dateIncrementSample.equals(dateBeginAppointmentSample) && dateIncrementSample.equals(dateEndAppointmentSample);

            final boolean condition2 = dateBeginIncrementWithHour.after(appointment.getBeginTime())
                    && dateBeginIncrementWithHour.before(appointment.getEndTime());
            final boolean condition3 = dateEndIncrementWithHour.after(appointment.getBeginTime())
                    && dateEndIncrementWithHour.before(appointment.getEndTime());

            final boolean subCondition41 = appointment.getBeginTime().after(dateBeginIncrementWithHour)
                    && appointment.getEndTime().before(dateEndIncrementWithHour);
            final boolean subCondition42 = dateBeginIncrementWithHour.equals(appointment.getBeginTime())
                    || dateBeginIncrementWithHour.equals(appointment.getEndTime());
            final boolean subCondition43 = dateEndIncrementWithHour.equals(appointment.getBeginTime())
                    || dateEndIncrementWithHour.equals(appointment.getEndTime());

            final boolean condition4 = subCondition41 || subCondition42 || subCondition43;

            final boolean subCondition1 = condition2 || condition3 || condition4;

            if (condition1 && subCondition1) {
                hasAppointment = true;
            }
        }

        return hasAppointment;
    }

    /**
     * Check holiday date.
     *
     * @param dateIncrement the date increment
     * @return true, if successful
     */
    private boolean checkHolidayDate(final Date dateIncrement) {

        boolean isHoliday = false;
        if (CollectionUtils.isNotEmpty(holidayList)) {
            for (final Holiday holiday : holidayList) {

                if (DateUtils.getDayOfYear(dateIncrement) == DateUtils.getDayOfYear(holiday.getHolidayDate())
                        && DateUtils.getYear(dateIncrement) == DateUtils.getYear(holiday.getHolidayDate())) {
                    isHoliday = true;
                    break;

                }
            }
        }
        return isHoliday;
    }

    /**
     * Gets the start date for proposed events.
     *
     * @return the start date for proposed events
     */
    public Date getStartDateForProposedEvents() {
        Date startDateForPropositions = null;
        final Date currentDate = Calendar.getInstance().getTime();
        DateUtils.setHourAndMinutes(currentDate, 0, 0);

        if (currentDate.before(calendarStartDate)) {
            startDateForPropositions = calendarStartDate;
        }
        if (currentDate.after(calendarStartDate) && currentDate.before(calendarEndtDate)) {
            startDateForPropositions = currentDate;
        }
        return startDateForPropositions;
    }

    /**
     * Gets the hourly type from date.
     *
     * @param date the date
     * @return the hourly type from date
     */
    public HourlyType getHourlyTypeFromDate(final Date date) {
        for (final WorkYearConfig workYearConfig : workYearConfigListOfCurrentYear) {
            if (DateUtils.isSameDay(date, workYearConfig.getBeginDate()) || DateUtils.isSameDay(date, workYearConfig.getEndDate())
                    || (date.after(workYearConfig.getBeginDate()) && date.before(workYearConfig.getEndDate()))) {
                return workYearConfig.getHourlyType();
            }
        }
        return null;

    }

    /**
     * Convert inspector list to inspector check list.
     *
     * @param inspectorList the inspector list
     * @return the list
     */
    private List<InspectorCheck> convertInspectorListToInspectorCheckList(final List<User> inspectorList) {
        inspectorCheckList = new ArrayList<InspectorCheck>();
        if (CollectionUtils.isNotEmpty(inspectorList)) {
            for (int i = 0; i < inspectorList.size(); i++) {

                final InspectorCheck inspectorCheck = new InspectorCheck(inspectorList.get(i), false, "insp" + i);
                inspectorCheckList.add(inspectorCheck);
            }
        }

        return inspectorCheckList;

    }

    /**
     * Fill holidays.
     *
     * @throws ParseException the parse exception
     */
    private void fillHolidays() throws ParseException {

        if (CollectionUtils.isNotEmpty(holidayList)) {
            for (final Holiday holiday : holidayList) {
                final String title = generateTitleForEvent(EVENT_HOLIDAY, null, holiday, null);
                final AppointmentEvent scheduleEvent = new AppointmentEvent();
                scheduleEvent.setTitle(title);
                scheduleEvent.setStartDate(new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS).parse(holiday.getHolidayDate()
                        + " 00:00:00"));
                scheduleEvent.setEndDate(new SimpleDateFormat(DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS).parse(holiday.getHolidayDate()
                        + " 23:59:59"));
                scheduleEvent.setStyleClass(EVENT_HOLIDAY);
                scheduleEvent.setBeginHour((short) 0);
                scheduleEvent.setBeginMinutes(0);
                scheduleEvent.setTypeEvent(EVENT_HOLIDAY);
                scheduleEvent.setEditable(false);
                scheduleEvent.setDescription(title);
                scheduleEvent.setAppointment(null);
                eventModel.addEvent(scheduleEvent);
            }
        }

    }

    /**
     * Divide dates by work year config.
     */
    private void divideDatesByWorkYearConfig() {

        workYearConfigListOfCurrentYear = workYearConfigService.findBetweenTwoDates(calendarStartDate, calendarEndtDate);

        workYearConfigList = new ArrayList<WorkYearConfig>();

        for (final WorkYearConfig workYearConfig : workYearConfigListOfCurrentYear) {

            //CHOSEN DATES ON CALENDAR ARE BETWEEN THE WORKYEARCONFIG
            if (calendarStartDate.after(workYearConfig.getBeginDate()) && workYearConfig.getEndDate().after(calendarEndtDate)) {
                final WorkYearConfig workYearConfigForAdd = new WorkYearConfig();
                workYearConfigForAdd.setBeginDate(calendarStartDate);
                workYearConfigForAdd.setEndDate(calendarEndtDate);
                workYearConfigForAdd.setHourlyType(workYearConfig.getHourlyType());
                workYearConfigList.add(workYearConfigForAdd);
            }

            //DATE END OF SCHEDULE OUT OF RANGE OF WORKYEARCONFIG
            if (calendarStartDate.after(workYearConfig.getBeginDate()) && calendarStartDate.before(workYearConfig.getEndDate())
                    && calendarEndtDate.after(workYearConfig.getEndDate())) {
                final WorkYearConfig workYearConfigForAdd = new WorkYearConfig();
                workYearConfigForAdd.setBeginDate(calendarStartDate);
                workYearConfigForAdd.setEndDate(workYearConfig.getEndDate());
                workYearConfigForAdd.setHourlyType(workYearConfig.getHourlyType());
                workYearConfigList.add(workYearConfigForAdd);
            }

            //DATE BEGIN OF SCHEDULE OUT OF RANGE OF WORKYEARCONFIG
            if (calendarStartDate.before(workYearConfig.getBeginDate()) && calendarEndtDate.after(workYearConfig.getBeginDate())
                    && calendarEndtDate.before(workYearConfig.getEndDate())) {
                final WorkYearConfig workYearConfigForAdd = new WorkYearConfig();
                workYearConfigForAdd.setBeginDate(workYearConfig.getBeginDate());
                workYearConfigForAdd.setEndDate(calendarEndtDate);
                workYearConfigForAdd.setHourlyType(workYearConfig.getHourlyType());
                workYearConfigList.add(workYearConfigForAdd);
            }

            //DATE END AND BEGIN OF SCHEDULE OUT OF RANGE OF WORKYEARCONFIG
            if (workYearConfig.getBeginDate().after(calendarStartDate) && calendarEndtDate.after(workYearConfig.getEndDate())) {
                final WorkYearConfig workYearConfigForAdd = new WorkYearConfig();
                workYearConfigForAdd.setBeginDate(workYearConfig.getBeginDate());
                workYearConfigForAdd.setEndDate(workYearConfig.getEndDate());
                workYearConfigForAdd.setHourlyType(workYearConfig.getHourlyType());
                workYearConfigList.add(workYearConfigForAdd);
            }

        }

    }

    /**
     * Adds the appointment to schedule.
     */
    public void addAppointmentToSchedule() {

        for (final Appointment appointment : appointmentList) {
            for (final InspectorCheck inspectorChecked : inspectorCheckList) {

                if (inspectorChecked.getController().getId().equals(appointment.getController().getId())
                        && inspectorChecked.getChecked()) {
                    final AppointmentEvent scheduleEvent = new AppointmentEvent();
                    final String title = generateTitleForEvent(EVENT_BUSY, appointment.getController(), null, appointment);
                    scheduleEvent.setTitle(title);
                    scheduleEvent.setDescription(title);
                    scheduleEvent.setStartDate(appointment.getBeginTime());
                    scheduleEvent.setEndDate(appointment.getEndTime());
                    scheduleEvent.setStyleClass(EVENT_BUSY);
                    scheduleEvent.setTypeEvent(EVENT_BUSY);
                    scheduleEvent.setEditable(false);
                    scheduleEvent.setAppointment(appointment);
                    scheduleEvent.setBeginHour(DateUtils.getHourFromDate(appointment.getBeginTime()));
                    scheduleEvent.setBeginMinutes(DateUtils.getMinutesFromDate(appointment.getBeginTime()));
                    scheduleEvent.setController(appointment.getController());

                    scheduleEvent.setAppointment(appointment);
                    eventModel.addEvent(scheduleEvent);
                    break;
                }

            }
        }

    }

    /**
     * Removes the appointment.
     *
     * @param appointmentEvent the appointment event
     */
    private void removeAppointment(final AppointmentEvent appointmentEvent) {
        for (final ScheduleEvent scheduleEvent : eventModel.getEvents()) {
            final Calendar scheduleEventCalendar = Calendar.getInstance();
            scheduleEventCalendar.setTime(scheduleEvent.getStartDate());
            final Calendar appointmentEventCalendar = Calendar.getInstance();
            appointmentEventCalendar.setTime(appointmentEvent.getStartDate());
            appointmentEventCalendar.set(Calendar.MILLISECOND, 0);
            scheduleEventCalendar.set(Calendar.MILLISECOND, 0);
            if (appointmentEventCalendar.getTime().compareTo(scheduleEventCalendar.getTime()) == 0) {
                eventModel.deleteEvent(scheduleEvent);
                break;
            }
        }

    }

    /**
     * Gets the current locale.
     *
     * @return the current locale
     */
    public Locale getCurrentLocale() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    /**
     * Gets the current service.
     *
     * @return the current service
     */
    public Service getCurrentService() {
        return currentService;
    }

    /**
     * Gets the random date.
     *
     * @param base the base
     * @return the random date
     */
    public Date getRandomDate(final Date base) {
        final Calendar date = Calendar.getInstance();
        date.setTime(base);
        //set random day of month
        date.add(Calendar.DATE, ((int) (Math.random() * Constants.THIRTY)) + Constants.ONE);

        return date.getTime();
    }

    /**
     * Gets the initial date.
     *
     * @return the initial date
     */
    public Date getInitialDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);

        return calendar.getTime();
    }

    /**
     * Gets the event model.
     *
     * @return the event model
     */
    public ScheduleModel getEventModel() {
        return eventModel;
    }

    /**
     * On event select.
     *
     * @param selectEvent the select event
     */
    public void onEventSelect(final SelectEvent selectEvent) {
        event = (AppointmentEvent) selectEvent.getObject();
        this.setSelectedEvent(event);
        carAvailableList = findAvailableCars(event.getStartDate(), event.getEndDate());
    }

    /**
     * On date select.
     *
     * @param selectEvent the select event
     */
    public void onDateSelect(final SelectEvent selectEvent) {

        event = new AppointmentEvent();
        event.setStartDate((Date) selectEvent.getObject());
        event.setEndDate(DateUtils.addHours(event.getStartDate(), DEFAULT_DURATION_INSPECTION));
        event.setTypeEvent(EVENT_NEW);
        event.setBeginHour(DateUtils.getHourFromDate(event.getStartDate()));
        event.setBeginMinutes(DateUtils.getMinutesFromDate(event.getStartDate()));

        final Appointment appointment = new Appointment();
        appointment.setBeginTime(event.getStartDate());
        appointment.setEndTime(event.getEndDate());
        event.setAppointment(appointment);
        carAvailableList = findAvailableCars(event.getStartDate(), event.getEndDate());

    }

    /**
     * Find available cars.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return the list
     */
    private List<Car> findAvailableCars(final Date startDate, final Date endDate) {
        carAvailableList = new ArrayList<>();
        final List<Car> indisponibleCars = new ArrayList<>();
        if (carList != null && !carList.isEmpty()) {
            final String dateIncrementSample = DateUtils.formatSimpleDate(startDate);

            for (final Appointment appointment : appointmentList) {
                final String dateBeginAppointmentSample = DateUtils.formatSimpleDate(appointment.getBeginTime());
                final String dateEndAppointmentSample = DateUtils.formatSimpleDate(appointment.getEndTime());

                final boolean dateIncSampBetweenDateBeginDateEndAppAppCarNotNull = dateIncrementSample
                        .equals(dateBeginAppointmentSample)
                        && dateIncrementSample.equals(dateEndAppointmentSample)
                        && appointment.getCar() != null;

                final boolean startDateBetweenAppBeginEndTime = startDate.after(appointment.getBeginTime())
                        && startDate.before(appointment.getEndTime());
                final boolean endDateBetweenAppBeginEndTime = endDate.after(appointment.getBeginTime())
                        && endDate.before(appointment.getEndTime());
                final boolean appointmentTimecheck = appointment.getBeginTime().after(startDate)
                        && appointment.getEndTime().before(endDate);

                final boolean check = startDateBetweenAppBeginEndTime || endDateBetweenAppBeginEndTime || appointmentTimecheck;

                if (dateIncSampBetweenDateBeginDateEndAppAppCarNotNull && check) {
                    indisponibleCars.add(appointment.getCar());
                }
            }

            for (final Car car : carList) {
                boolean found = false;
                for (final Car carInsp : indisponibleCars) {
                    if (carInsp.getId().equals(car.getId())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    carAvailableList.add(car);
                }
            }

        }

        return carAvailableList;
    }

    /**
     * Gets the appointment service.
     *
     * @return the appointment service
     */
    public AppointmentService getAppointmentService() {
        return appointmentService;
    }

    /**
     * Sets the appointment service.
     *
     * @param appointmentService the new appointment service
     */
    public void setAppointmentService(final AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Gets the inspection work week config service.
     *
     * @return the inspection work week config service
     */
    public InspectionWorkWeekConfigService getInspectionWorkWeekConfigService() {
        return inspectionWorkWeekConfigService;
    }

    /**
     * Sets the inspection work week config service.
     *
     * @param inspectionWorkWeekConfigService the new inspection work week
     * config service
     */
    public void setInspectionWorkWeekConfigService(final InspectionWorkWeekConfigService inspectionWorkWeekConfigService) {
        this.inspectionWorkWeekConfigService = inspectionWorkWeekConfigService;
    }

    /**
     * Gets the inspection work day config service.
     *
     * @return the inspection work day config service
     */
    public InspectionWorkDayConfigService getInspectionWorkDayConfigService() {
        return inspectionWorkDayConfigService;
    }

    /**
     * Sets the inspection work day config service.
     *
     * @param inspectionWorkDayConfigService the new inspection work day config
     * service
     */
    public void setInspectionWorkDayConfigService(final InspectionWorkDayConfigService inspectionWorkDayConfigService) {
        this.inspectionWorkDayConfigService = inspectionWorkDayConfigService;
    }

    /**
     * Gets the work year config service.
     *
     * @return the work year config service
     */
    public WorkYearConfigService getWorkYearConfigService() {
        return workYearConfigService;
    }

    /**
     * Sets the work year config service.
     *
     * @param workYearConfigService the new work year config service
     */
    public void setWorkYearConfigService(final WorkYearConfigService workYearConfigService) {
        this.workYearConfigService = workYearConfigService;
    }

    /**
     * Gets the appointment list.
     *
     * @return the appointment list
     */
    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    /**
     * Sets the appointment list.
     *
     * @param appointmentList the new appointment list
     */
    public void setAppointmentList(final List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    /**
     * Gets the calendar start date.
     *
     * @return the calendar start date
     */
    public Date getCalendarStartDate() {
        return calendarStartDate;
    }

    /**
     * Sets the calendar start date.
     *
     * @param calendarStartDate the new calendar start date
     */
    public void setCalendarStartDate(final Date calendarStartDate) {
        this.calendarStartDate = calendarStartDate;
    }

    /**
     * Gets the calendar endt date.
     *
     * @return the calendar endt date
     */
    public Date getCalendarEndtDate() {
        return calendarEndtDate;
    }

    /**
     * Sets the calendar endt date.
     *
     * @param calendarEndtDate the new calendar endt date
     */
    public void setCalendarEndtDate(final Date calendarEndtDate) {
        this.calendarEndtDate = calendarEndtDate;
    }

    /**
     * Sets the event model.
     *
     * @param eventModel the new event model
     */
    public void setEventModel(final ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    /**
     * Gets the event.
     *
     * @return the event
     */
    public ScheduleEvent getEvent() {
        return event;
    }

    /**
     * Gets the holiday service.
     *
     * @return the holiday service
     */
    public HolidayService getHolidayService() {
        return holidayService;
    }

    /**
     * Sets the holiday service.
     *
     * @param holidayService the new holiday service
     */
    public void setHolidayService(final HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    /**
     * Gets the holiday list.
     *
     * @return the holiday list
     */
    public List<Holiday> getHolidayList() {
        return holidayList;
    }

    /**
     * Sets the holiday list.
     *
     * @param holidayList the new holiday list
     */
    public void setHolidayList(final List<Holiday> holidayList) {
        this.holidayList = holidayList;
    }

    /**
     * Gets the current year.
     *
     * @return the current year
     */
    public Integer getCurrentYear() {
        return currentYear;
    }

    /**
     * Sets the current year.
     *
     * @param currentYear the new current year
     */
    public void setCurrentYear(final Integer currentYear) {
        this.currentYear = currentYear;
    }

    /**
     * Gets the work year config list of current year.
     *
     * @return the work year config list of current year
     */
    public List<WorkYearConfig> getWorkYearConfigListOfCurrentYear() {
        return workYearConfigListOfCurrentYear;
    }

    /**
     * Sets the work year config list of current year.
     *
     * @param workYearConfigListOfCurrentYear the new work year config list of
     * current year
     */
    public void setWorkYearConfigListOfCurrentYear(final List<WorkYearConfig> workYearConfigListOfCurrentYear) {
        this.workYearConfigListOfCurrentYear = workYearConfigListOfCurrentYear;
    }

    /**
     * Gets the work year config list.
     *
     * @return the work year config list
     */
    public List<WorkYearConfig> getWorkYearConfigList() {
        return workYearConfigList;
    }

    /**
     * Sets the work year config list.
     *
     * @param workYearConfigList the new work year config list
     */
    public void setWorkYearConfigList(final List<WorkYearConfig> workYearConfigList) {
        this.workYearConfigList = workYearConfigList;
    }

    /**
     * Gets the user service.
     *
     * @return the user service
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Sets the user service.
     *
     * @param userService the new user service
     */
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets the inspector list.
     *
     * @return the inspector list
     */
    public List<User> getInspectorList() {
        return inspectorList;
    }

    /**
     * Sets the inspector list.
     *
     * @param inspectorList the new inspector list
     */
    public void setInspectorList(final List<User> inspectorList) {
        this.inspectorList = inspectorList;
    }

    /**
     * Gets the inspector check list.
     *
     * @return the inspector check list
     */
    public List<InspectorCheck> getInspectorCheckList() {
        return inspectorCheckList;
    }

    /**
     * Sets the inspector check list.
     *
     * @param inspectorCheckList the new inspector check list
     */
    public void setInspectorCheckList(final List<InspectorCheck> inspectorCheckList) {
        this.inspectorCheckList = inspectorCheckList;
    }

    /**
     * Gets the inspection work day configs list.
     *
     * @return the inspection work day configs list
     */
    public List<InspectionWorkDayConfig> getInspectionWorkDayConfigsList() {
        return inspectionWorkDayConfigsList;
    }

    /**
     * Sets the inspection work day configs list.
     *
     * @param inspectionWorkDayConfigsList the new inspection work day configs
     * list
     */
    public void setInspectionWorkDayConfigsList(final List<InspectionWorkDayConfig> inspectionWorkDayConfigsList) {
        this.inspectionWorkDayConfigsList = inspectionWorkDayConfigsList;
    }

    /**
     * Gets the proposed events.
     *
     * @return the proposed events
     */
    public List<AppointmentEvent> getProposedEvents() {
        return proposedEvents;
    }

    /**
     * Sets the proposed events.
     *
     * @param proposedEvents the new proposed events
     */
    public void setProposedEvents(final List<AppointmentEvent> proposedEvents) {
        this.proposedEvents = proposedEvents;
    }

    /**
     * Gets the inspection work week config list.
     *
     * @return the inspection work week config list
     */
    public List<InspectionWorkWeekConfig> getInspectionWorkWeekConfigList() {
        return inspectionWorkWeekConfigList;
    }

    /**
     * Sets the inspection work week config list.
     *
     * @param inspectionWorkWeekConfigList the new inspection work week config
     * list
     */
    public void setInspectionWorkWeekConfigList(final List<InspectionWorkWeekConfig> inspectionWorkWeekConfigList) {
        this.inspectionWorkWeekConfigList = inspectionWorkWeekConfigList;
    }

    /**
     * Sets the current service.
     *
     * @param currentService the new current service
     */
    public void setCurrentService(final Service currentService) {
        this.currentService = currentService;
    }

    /**
     * Gets the controller holiday service.
     *
     * @return the controllerHolidayService
     */
    public ControllerHolidayService getControllerHolidayService() {
        return controllerHolidayService;
    }

    /**
     * Sets the controller holiday service.
     *
     * @param controllerHolidayService the controllerHolidayService to set
     */
    public void setControllerHolidayService(final ControllerHolidayService controllerHolidayService) {
        this.controllerHolidayService = controllerHolidayService;
    }

    /**
     * Gets the car service.
     *
     * @return the car service
     */
    public CarService getCarService() {
        return carService;
    }

    /**
     * Sets the car service.
     *
     * @param carService the new car service
     */
    public void setCarService(final CarService carService) {
        this.carService = carService;
    }

    /**
     * Gets the car list.
     *
     * @return the car list
     */
    public List<Car> getCarList() {
        return carList;
    }

    /**
     * Sets the car list.
     *
     * @param carList the new car list
     */
    public void setCarList(final List<Car> carList) {
        this.carList = carList;
    }

    /**
     * Sets the event.
     *
     * @param event the new event
     */
    public void setEvent(final AppointmentEvent event) {
        this.event = event;
    }

    /**
     * Gets the car available list.
     *
     * @return the car available list
     */
    public List<Car> getCarAvailableList() {
        return carAvailableList;
    }

    /**
     * Sets the car available list.
     *
     * @param carAvailableList the new car available list
     */
    public void setCarAvailableList(final List<Car> carAvailableList) {
        this.carAvailableList = carAvailableList;
    }

    /**
     * Gets the added event.
     *
     * @return the added event
     */
    public AppointmentEvent getAddedEvent() {
        return addedEvent;
    }

    /**
     * Sets the added event.
     *
     * @param addedEvent the new added event
     */
    public void setAddedEvent(final AppointmentEvent addedEvent) {
        this.addedEvent = addedEvent;
    }

    /**
     * Gets the selected event.
     *
     * @return the selected event
     */
    public AppointmentEvent getSelectedEvent() {
        return selectedEvent;
    }

    /**
     * Sets the selected event.
     *
     * @param selectedEvent the new selected event
     */
    public void setSelectedEvent(final AppointmentEvent selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    /**
     * Checks if is deleted event.
     *
     * @return true, if is deleted event
     */
    public boolean isDeletedEvent() {
        return deletedEvent;
    }

    /**
     * Sets the deleted event.
     *
     * @param deletedEvent the new deleted event
     */
    public void setDeletedEvent(final boolean deletedEvent) {
        this.deletedEvent = deletedEvent;
    }

    /**
     * Checks if is rdv confirmation.
     *
     * @return true, if is rdv confirmation
     */
    public boolean isRdvConfirmation() {
        return rdvConfirmation;
    }

    /**
     * Sets the rdv confirmation.
     *
     * @param rdvConfirmation the new rdv confirmation
     */
    public void setRdvConfirmation(final boolean rdvConfirmation) {
        this.rdvConfirmation = rdvConfirmation;
    }

}
