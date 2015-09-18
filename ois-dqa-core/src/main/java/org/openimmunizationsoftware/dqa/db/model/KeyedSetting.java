/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.db.model;

import java.io.Serializable;
import java.util.Date;

public class KeyedSetting implements Serializable
{
  
  private static final long serialVersionUID = 1l;
  
  public static final String APPLICATION_EXTERNAL_URL_BASE = "application.external.url.base";
  public static final String DATABASE_CLEANUP_ENABLED = "database_cleanup.enabled";
  public static final String DATABASE_CLEANUP_DAY = "database_cleanup.day";
  public static final String DATABASE_CLEANUP_END_TIME = "database_cleanup.end_time";
  public static final String DATABASE_CLEANUP_START_TIME = "database_cleanup.start_time";
  public static final String DATABASE_CLEANUP_DATA_FIELDS_AFTER_DAYS = "database_cleanup.data_fields.after_days";
  public static final String DATABASE_CLEANUP_MESSAGE_TEXT_AFTER_DAYS = "database_cleanup.message_text.after_days";
  public static final String DATABASE_CLEANUP_MESSAGE_ANALYSIS_AFTER_DAYS = "database_cleanup.message_analysis.after_days";
  public static final String DATABASE_CLEANUP_BATCH_REPORTS_SUBMITTED_AFTER_DAYS = "database_cleanup.batch_reports_submitted.after_days";
  public static final String DATABASE_CLEANUP_BATCH_REPORTS_WEEKLY_AFTER_DAYS = "database_cleanup.batch_reports_weekly.after_days";
  public static final String DATABASE_CLEANUP_SUBMISSIONS_AFTER_DAYS = "database_cleanup.submissions.after_days";
  public static final String IN_FILE_ACCEPTED_DIR_NAME = "in.file.accepted_dir.name";
  public static final String IN_FILE_DIR = "in.file.dir";
  public static final String IN_FILE_DQA_DIR_NAME = "in.file.dqa_dir.name";
  public static final String IN_FILE_ENABLE = "in.file.enabled";
  public static final String IN_FILE_EXPORT_CONNECTION_SCRIPT = "in.file.export.connection_script";
  public static final String IN_FILE_RECEIVE_DIR_NAME = "in.file.receive_dir.name";
  public static final String IN_FILE_SUBMIT_DIR_NAME = "in.file.submit_dir.name";
  public static final String IN_FILE_THREAD_COUNT_MAX = "in.file.thread.count.max";
  public static final String IN_FILE_WAIT = "in.file.wait";
  public static final String IN_SUBMISSION_ENABLE = "in.submission.enabled";
  public static final String IN_SUBMISSION_WAIT = "in.submission.wait";
  public static final String IN_SUBMISSION_VERIFY_UNIQUE_CONTROL_ID = "in.submission.verify-unique-control-id";
  public static final String OUT_FILE_DIR = "out.file.dir";
  public static final String OUT_HL7_MSH_PROCESSING_ID = "out.hl7.msh.processing_id";
  public static final String OUT_HL7_MSH_RECEIVING_APPLICATION = "out.hl7.msh.receiving_application";
  public static final String OUT_HL7_MSH_RECEIVING_FACILITY = "out.hl7.msh.receiving_facility";
  public static final String OUT_HL7_MSH_SENDING_APPLICATION = "out.hl7.msh.sending_application";
  public static final String OUT_HL7_MSH_VERSION_ID = "out.hl7.msh.version_id";
  public static final String VALIDATE_HEADER_SENDING_FACILITY_MAX_LEN = "validate.header.sending_facility.max_len";
  public static final String VALIDATE_HEADER_SENDING_FACILITY_MIN_LEN = "validate.header.sending_facility.min_len";
  public static final String VALIDATE_HEADER_SENDING_FACILITY_NUMERIC = "validate.header.sending_facility.numeric";
  public static final String VALIDATE_HEADER_SENDING_FACILITY_PFS = "validate.header.sending_facility.pfs";
  public static final String VALIDATE_VACCINATION_FACILITY_MAX_LEN = "validate.vaccination.facility.max_len";
  public static final String VALIDATE_VACCINATION_FACILITY_MIN_LEN = "validate.vaccination.facility.min_len";
  public static final String VALIDATE_VACCINATION_FACILITY_NUMERIC = "validate.vaccination.facility.numeric";
  public static final String VALIDATE_VACCINATION_FACILITY_PFS = "validate.vaccination.facility.pfs";
  public static final String VALIDATE_PATIENT_FINANCIAL_STATUS_IGNORE = "validate.patient.financialStatus.ignore";
  public static final String WEEKLY_BATCH_DAY = "weekly.batch.day";
  public static final String WEEKLY_BATCH_END_TIME = "weekly.batch.end_time";
  public static final String WEEKLY_BATCH_START_TIME = "weekly.batch.start_time";
  public static final String WEEKLY_EXPORT_DAY_HIGH = "weekly.export.day.high";
  public static final String WEEKLY_EXPORT_DAY_HIGHEST = "weekly.export.day.highest";
  public static final String WEEKLY_EXPORT_DAY_LOW = "weekly.export.day.low";
  public static final String WEEKLY_EXPORT_DAY_LOWEST = "weekly.export.day.lowest";
  public static final String WEEKLY_EXPORT_DAY_NORMAL = "weekly.export.day.normal";
  public static final String WEEKLY_EXPORT_END_TIME = "weekly.export.end_time";
  public static final String WEEKLY_EXPORT_START_TIME = "weekly.export.start_time";
  public static final String UPLOAD_ENABLED = "upload.enabled";
  public static final String UPLOAD_DIR = "upload.dir";
  public static final String REMOTE_ENABLED = "remote.enabled";
  public static final String DQA_REPORT_READY_FOR_PRODUCTION_ENABLED = "dqa_report.ready_for_production.enabled";
  public static final String DQA_REPORT_READY_FOR_PRODUCTION_TRIGGER_LEVEL = "dqa_report.ready_for_production.trigger_level";
  public static final String CDS_SOFTWARE_SERVICE_ENABLED = "cds_software.service.enabled";
  public static final String CDS_SOFTWARE_SERVICE_URL = "cds_software.service_url";
  public static final String CDS_SOFTWARE_SERVICE_TYPE = "cds_software.service_type";
  public static final String CDS_SOFTWARE_SCHEDULE_NAME = "cds_software.schedule_name"; 

  private String keyedCode = "";
  private int keyedId = 0;
  private String keyedValue = "";
  private String objectCode = "";
  private int objectId = 0;
  
  public String getKeyedCode()
  {
    return keyedCode;
  }
  public int getKeyedId()
  {
    return keyedId;
  }
  public String getKeyedValue()
  {
    return keyedValue;
  }
  public String getObjectCode()
  {
    return objectCode;
  }
  public int getObjectId()
  {
    return objectId;
  }
  public void setKeyedCode(String keyedCode)
  {
    this.keyedCode = keyedCode;
  }
  public void setKeyedId(int keyedId)
  {
    this.keyedId = keyedId;
  }
  public void setKeyedValue(String keyedValue)
  {
    this.keyedValue = keyedValue;
  }
  public void setObjectCode(String objectCode)
  {
    this.objectCode = objectCode;
  }
  public void setObjectId(int objectId)
  {
    this.objectId = objectId;
  }
}
