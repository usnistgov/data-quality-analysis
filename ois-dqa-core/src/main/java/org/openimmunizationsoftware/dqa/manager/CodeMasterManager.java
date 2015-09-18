/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.manager;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.openimmunizationsoftware.dqa.db.model.CodeMaster;
import org.openimmunizationsoftware.dqa.db.model.CodeTable;
import org.openimmunizationsoftware.dqa.db.model.received.types.CodedEntity;

public class CodeMasterManager
{
  public static final String ACKNOWLEDGEMENT_TYPE_ALWAYS = "AL";
  public static final String ACKNOWLEDGEMENT_TYPE_ERROR_ONLY = "ER";
  public static final String ACKNOWLEDGEMENT_TYPE_NEVER = "NE";
  public static final String ACKNOWLEDGEMENT_TYPE_SU = "SU";
  public static final String ADDRESS_TYPE_FIRM_BUSINESS = "B";
  public static final String ADDRESS_TYPE_BAD_ADDRESS = "BA";
  public static final String ADDRESS_TYPE_BIRTH_DELIVERY_LOCATION = "BDL";
  public static final String ADDRESS_TYPE_RESIDENCE_AT_BIRTH = "BR";
  public static final String ADDRESS_TYPE_CURRENT_OR_TEMPORARY = "C";
  public static final String ADDRESS_TYPE_COUNTRY_OF_ORIGIN = "F";
  public static final String ADDRESS_TYPE_HOME = "H";
  public static final String ADDRESS_TYPE_LEGAL_ADDRESS = "L";
  public static final String ADDRESS_TYPE_MAILING = "M";
  public static final String ADDRESS_TYPE_BIRTH_NEE = "N";
  public static final String ADDRESS_TYPE_OFFICE = "O";
  public static final String ADDRESS_TYPE_PERMANENT = "P";
  public static final String ADDRESS_TYPE_REGISTRY_HOME = "RH";

  public static final String ADMINISTRATION_UNIT_MILLILITER = "ML";

  public static final String BODY_ROUTE_INTRADERMAL = "ID";
  public static final String BODY_ROUTE_INTRAMUSCULAR = "IM";

  public static final String BODY_ROUTE_INTRAVENOUS = "IV";
  public static final String BODY_ROUTE_NASAL = "NS";
  public static final String BODY_ROUTE_OTHER_MISCELLANEOUS = "OTH";
  public static final String BODY_ROUTE_ORAL = "PO";
  public static final String BODY_ROUTE_SUBCUTANEOUS = "SC";
  public static final String BODY_ROUTE_TRANSDERMAL = "TD";
  public static final String BODY_SITE_LEFT_UPPER_ARM = "LA";
  public static final String BODY_SITE_LEFT_DELTOID = "LD";
  public static final String BODY_SITE_LEFT_GLUTEOUS_MEDIUS = "LG";
  public static final String BODY_SITE_LEFT_LOWER_FOREARM = "LLFA";
  public static final String BODY_SITE_LEFT_THIGH = "LT";
  public static final String BODY_SITE_LEFT_VASTUS_LATERALIS = "LVL";
  public static final String BODY_SITE_RIGHT_UPPER_ARM = "RA";
  public static final String BODY_SITE_RIGHT_DELTOID = "RD";
  public static final String BODY_SITE_RIGHT_GLUTEOUS_MEDIUS = "RG";
  public static final String BODY_SITE_RIGHT_LOWER_FOREARM = "RLFA";
  public static final String BODY_SITE_RIGHT_THIGH = "RT";
  public static final String BODY_SITE_RIGHT_VASTUS_LATERALIS = "RVL";

  public static final String HL7_CODING_SYSTEM_SEX = "HL70001";
  public static final String HL7_CODING_SYSTEM_EVENT_TYPE = "HL70003";
  public static final String HL7_CODING_SYSTEM_PATIENT_CLASS = "HL70004";
  public static final String HL7_CODING_SYSTEM_RACE = "HL70005";
  public static final String HL7_CODING_SYSTEM_ACKNOWLEDGEMENT_CODE = "HL70008";
  public static final String HL7_CODING_SYSTEM_PHYSICIAN_ID = "HL70010";
  public static final String HL7_CODING_SYSTEM_CHECK_DIGIT_SCHEME = "HL70061";
  public static final String HL7_CODING_SYSTEM_RELATIONSHIP = "HL70063";
  public static final String HL7_CODING_SYSTEM_FINANCIAL_CLASS = "HL70064";
  public static final String HL7_CODING_SYSTEM_MESSAGE_TYPE = "HL70076";
  public static final String HL7_CODING_SYSTEM_ABNORMAL_FLAGS = "HL70078";
  public static final String HL7_CODING_SYSTEM_OBSERVATION_RESULT_STATUS_CODES_INTERPRETATION = "HL70085";
  public static final String HL7_CODING_SYSTEM_QUERY_PRIORITY = "HL70091";
  public static final String HL7_CODING_SYSTEM_DELAYED_ACKNOWLEDGMENT_TYPE = "HL70102";
  public static final String HL7_CODING_SYSTEM_PROCESSING_ID = "HL70103";
  public static final String HL7_CODING_SYSTEM_VERSION_ID = "HL70104";
  public static final String HL7_CODING_SYSTEM_SOURCE_OF_COMMENT = "HL70105";
  public static final String HL7_CODING_SYSTEM_ORDER_CONTROL_CODES = "HL70199";
  public static final String HL7_CODING_SYSTEM_QUANTITY_LIMITED_REQUEST = "HL70126";
  public static final String HL7_CODING_SYSTEM_YES_NO_INDICATOR = "HL70136";
  public static final String HL7_CODING_SYSTEM_ACCEPT_APPLICATION_ACKNOWLEDGMENT_CONDITIONS = "HL70155";
  public static final String HL7_CODING_SYSTEM_ROUTE_OF_ADMINISTRATION = "HL70162";
  public static final String HL7_CODING_SYSTEM_ADMINISTRATIVE_SITE = "HL70163";
  public static final String HL7_CODING_SYSTEM_ETHNIC_GROUP = "HL70189";
  public static final String HL7_CODING_SYSTEM_ADDRESS_TYPE = "HL70190";
  public static final String HL7_CODING_SYSTEM_NAME_TYPE = "HL70200";
  public static final String HL7_CODING_SYSTEM_TELECOMMUNICATION_USE_CODE = "HL70201";
  public static final String HL7_CODING_SYSTEM_TELECOMMUNICATION_EQUIPMENT_TYPE = "HL70202";
  public static final String HL7_CODING_SYSTEM_IDENTIFIER_TYPE = "HL70203";
  public static final String HL7_CODING_SYSTEM_ORGANIZATIONAL_NAME_TYPE = "HL70204";
  public static final String HL7_CODING_SYSTEM_PROCESING_MODE = "HL70207";
  public static final String HL7_CODING_SYSTEM_QUERY_RESPONSE_STATUS = "HL70208";
  public static final String HL7_CODING_SYSTEM_ALTERNATE_CHARACTER_SETS = "HL70211";
  public static final String HL7_CODING_SYSTEM_PUBLICITY_CODE = "HL70215";
  public static final String HL7_CODING_SYSTEM_LIVING_ARRANGEMENT = "HL70220";
  public static final String HL7_CODING_SYSTEM_MANUFACTURERS_OF_VACCINES = "MVX";

  public static final String HL7_CODING_SYSTEM_CENSUS_TRACT = "HL70228";
  public static final String HL7_CODING_SYSTEM_COUNTY_PARISH = "HL70289";

  public static final String HL7_CODING_SYSTEM_CODES_FOR_VACCINES_ADMINISTERED = "CVX";
  public static final String HL7_CODING_SYSTEM_LANGUAGE = "HL70296";
  public static final String HL7_CODING_SYSTEM_CN_ID_SOURCE = "HL70297";
  public static final String HL7_CODING_SYSTEM_NAMESPACE_ID = "HL70300";
  public static final String HL7_CODING_SYSTEM_COMPLETION_STATUS = "HL70322";
  public static final String HL7_CODING_SYSTEM_ACTION_CODE = "HL70323";
  public static final String HL7_CODING_SYSTEM_MESSAGE_STRUCTURE = "HL70354";
  public static final String HL7_CODING_SYSTEM_ALTERNATE_CHARACTER_SET_HANDLING_SCHEME = "HL70356";
  public static final String HL7_CODING_SYSTEM_MESSAGE_ERROR_STATUS_CODE = "HL70357";
  public static final String HL7_CODING_SYSTEM_DEGREE = "HL70360";
  public static final String HL7_CODING_SYSTEM_APPLICATION = "HL70361";
  public static final String HL7_CODING_SYSTEM_FACILITY = "HL70362";
  public static final String HL7_CODING_SYSTEM_ASSIGNING_AUTHORITY = "HL70363";
  public static final String HL7_CODING_SYSTEM_CODING_SYSTEM = "HL70396";
  public static final String HL7_CODING_SYSTEM_IMMUNIZATION_REGISTRY_STATUS = "HL70441";
  public static final String HL7_CODING_SYSTEM_QUERY_NAME = "HL70471";
  public static final String HL7_CODING_SYSTEM_ERROR_SEVERITY = "HL70516";
  public static final String HL7_CODING_SYSTEM_APPLICATION_ERROR_CODE = "HL70533";
  public static final String HL7_CODING_SYSTEM_IMMUNIZATION_INFORMATION_SOURCE = "NIP001";
  public static final String HL7_CODING_SYSTEM_SUBSTANCE_REFUSAL_REASON = "NIP002";
  public static final String HL7_CODING_SYSTEM_OBSERVATION_IDENTIFIERS = "NIP003";
  public static final String HL7_CODING_SYSTEM_CONTRAINDICATIONS_PRECAUTIONS_AND_IMMUNITIES = "NIP004";

  public static final String HL7_VALUE_TYPE_CODED_ENTRY = "CE";

  public static final String ID_TYPE_CODE_ACCOUNT_NUMBER = "AN";
  public static final String ID_TYPE_CODE_ANONYMOUS_IDENTIFIER = "ANON";
  public static final String ID_TYPE_CODE_ACCOUNT_NUMBER_CREDITOR = "ANC";
  public static final String ID_TYPE_CODE_ACCOUNT_NUMBER_DEBITOR = "AND";
  public static final String ID_TYPE_CODE_TEMPORARY_ACCOUNT_NUMBER = "ANT";
  public static final String ID_TYPE_CODE_ADVANCED_PRACTICE_REGISTERED_NURSE_NUMBER = "APRN";
  public static final String ID_TYPE_CODE_BANK_ACCOUNT_NUMBER = "BA";
  public static final String ID_TYPE_CODE_BANK_CARD_NUMBER = "BC";
  public static final String ID_TYPE_CODE_BIRTH_REGISTRY_NUMBER = "BR";
  public static final String ID_TYPE_CODE_COST_CENTER_NUMBER = "CC";
  public static final String ID_TYPE_CODE_COUNTY_NUMBER = "CY";
  public static final String ID_TYPE_CODE_DENTIST_LICENSE_NUMBER = "DDS";
  public static final String ID_TYPE_CODE_DRUG_ENFORCEMENT_ADMINISTRATION_REGISTRATION_NUMBER = "DEA";
  public static final String ID_TYPE_CODE_DRUG_FURNISHNG_OR_PRESCRIPTIVE_AUTHORITY_NUMBER = "DFN";
  public static final String ID_TYPE_CODE_DRIVERS_LICENSE_NUMBER = "DL";
  public static final String ID_TYPE_CODE_DOCTOR_NUMBER = "DN";
  public static final String ID_TYPE_CODE_PODIATRIST_LICENSE_NUMBER = "DPM";
  public static final String ID_TYPE_CODE_OSTEOPATHIC_LICENSE_NUMBER = "DO";
  public static final String ID_TYPE_CODE_DONOR_REGISTRATION_NUMBER = "DR";
  public static final String ID_TYPE_CODE_EMPLOYEE_NUMBER = "EI";
  public static final String ID_TYPE_CODE_EMPLOYER_NUMBER = "EN";
  public static final String ID_TYPE_CODE_FACILITY_ID = "FI";
  public static final String ID_TYPE_CODE_GUARANTOR_INTERNAL_IDENTIFIER = "GI";
  public static final String ID_TYPE_CODE_GENERAL_LEDGER_NUMBER = "GL";
  public static final String ID_TYPE_CODE_GUARANTOR_EXTERNAL_IDENTIFIER = "GN";
  public static final String ID_TYPE_CODE_HEALTH_CARD_NUMBER = "HC";
  public static final String ID_TYPE_CODE_JURISDICTIONAL_HEALTH_NUMBER_CANADA = "JHN";
  public static final String ID_TYPE_CODE_INDIGENOUSE_ABORIGINAL = "IND";
  public static final String ID_TYPE_CODE_LABOR_AND_INDUSTRIES_NUMBER = "LI";
  public static final String ID_TYPE_CODE_LICENSE_NUMBER = "LN";
  public static final String ID_TYPE_CODE_LOCAL_REGISTRY_ID = "LR";
  public static final String ID_TYPE_CODE_PATIENT_MEDICAID_NUMBER = "MA";
  public static final String ID_TYPE_CODE_MEMBER_NUMBER = "MB";
  public static final String ID_TYPE_CODE_PATIENTS_MEDICARE_NUMBER = "MC";
  public static final String ID_TYPE_CODE_PRACTIONER_MEDICAID_NUMBER = "MCD";
  public static final String ID_TYPE_CODE_MICROCHIP_NUMBER = "MCN";
  public static final String ID_TYPE_CODE_PRACTITIONER_MEDICARE_NUMBER = "MCR";
  public static final String ID_TYPE_CODE_MEDICAL_LICENSE_NUMBER = "MD";
  public static final String ID_TYPE_CODE_MILITARY_ID_NUMBER = "MI";
  public static final String ID_TYPE_CODE_MEDICAL_RECORD_NUMBER = "MR";
  public static final String ID_TYPE_CODE_TEMPORARY_MEDICAL_RECORD_NUMBER = "MRT";
  public static final String ID_TYPE_CODE_NATIONAL_EMPLOYER_IDENTIFIER = "NE";
  public static final String ID_TYPE_CODE_NATIONAL_HEALTH_PLAN_IDENTIFIER = "NH";
  public static final String ID_TYPE_CODE_NATIONAL_UNIQUE_INDIVIDUAL_IDENTIFIER = "NI";
  public static final String ID_TYPE_CODE_NATIONAL_INSURANCE_ORGANIZATION_IDENTIFIER = "NII";
  public static final String ID_TYPE_CODE_NATIONAL_INSURANCE_PAYOR_IDENTIFIER = "NIIP";
  public static final String ID_TYPE_CODE_NURSE_PRACTITIONER_NUMBER = "NP";
  public static final String ID_TYPE_CODE_NATIONAL_PROVIDER_IDENTIFIER = "NPI";
  public static final String ID_TYPE_CODE_OPTOMETRIST_LICENSE_NUMBER = "OD";
  public static final String ID_TYPE_CODE_PHYSICIAN_ASSISTANT_NUMBER = "PA";
  public static final String ID_TYPE_CODE_PENITENTIARY_CORRECTIONAL_INSTITUTION_NUMBER = "PCN";
  public static final String ID_TYPE_CODE_LIVING_SUBJECT_ENTERPRISE_NUMBER = "PE";
  public static final String ID_TYPE_CODE_PENSION_NUMBER = "PEN";
  public static final String ID_TYPE_CODE_PATIENT_INTERNAL_IDENTIFIER = "PI";
  public static final String ID_TYPE_CODE_PERSON_NUMBER = "PN";
  public static final String ID_TYPE_CODE_TEMPORARY_LIVING_SUBJECT_NUMBER = "PNT";
  public static final String ID_TYPE_CODE_PASSPORT_NUMBER = "PPN";
  public static final String ID_TYPE_CODE_PERMANENT_RESIDENT_CARD_NUMBER = "PRC";
  public static final String ID_TYPE_CODE_PROVIDER_NUMBER = "PRN";
  public static final String ID_TYPE_CODE_PATIENT_EXTERNAL_IDENTIFIER = "PT";
  public static final String ID_TYPE_CODE_QA_NUMBER = "QA";
  public static final String ID_TYPE_CODE_RESOURCE_IDENTIFIER = "RI";
  public static final String ID_TYPE_CODE_PHARMACIST_LICENSE_NUMBER = "RPH";
  public static final String ID_TYPE_CODE_REGISTERED_NURSE_NUMBER = "RN";
  public static final String ID_TYPE_CODE_RAILROAD_RETIREMENT_NUMBER = "RR";
  public static final String ID_TYPE_CODE_REGIONAL_REGISTRY_ID = "RRI";
  public static final String ID_TYPE_CODE_STATE_LICENSE = "SL";
  public static final String ID_TYPE_CODE_SUBSRIBER_NUMBER = "SN";
  public static final String ID_TYPE_CODE_STATE_REGISTRY_ID = "SR";
  public static final String ID_TYPE_CODE_SOCIAL_SECURITY_NUMBER = "SS";
  public static final String ID_TYPE_CODE_TAX_ID_NUMBER = "TAX";
  public static final String ID_TYPE_CODE_UNSPECIFIED_IDENTIFIER = "U";
  public static final String ID_TYPE_CODE_MEDICARE_CMS_UNIVERSAL_PHYSICIAN_IDENTIFICATION_NUMBERS = "UPIN";
  public static final String ID_TYPE_CODE_VISIT_NUMBER = "VN";
  public static final String ID_TYPE_CODE_WIC_IDENTIFIER = "WC";
  public static final String ID_TYPE_CODE_WORKERS_COMP_NUMBER = "WCN";
  public static final String ID_TYPE_CODE_ORGANIZATION_IDENTIFIER = "XX";
  public static final String MESSAGE_PROCESSING_ID_DEBUG = "D";
  public static final String MESSAGE_PROCESSING_ID_PRODUCTION = "P";
  public static final String MESSAGE_PROCESSING_ID_TRAINING = "T";
  public static final String OBSERVATION_IDENTIFIER_VACCINE_FUNDING_PROGRAM_ELIGIBILITY_CATEGORY = "64994-7";
  public static final String OBSERVATION_IDENTIFIER_VACCINE_FUNDING_SOURCE = "30963-3";
  public static final String OBSERVATION_IDENTIFIER_VACCINE_TYPE = "30956-7";
  public static final String OBSERVATION_IDENTIFIER_COMPONENT_VACCINE_TYPE = "38890-0";
  public static final String OBSERVATION_IDENTIFIER_VACCINATION_CONTRAINDICATION_PRECAUTION_EFFECTIVE_DATE = "30946-8";
  public static final String OBSERVATION_IDENTIFIER_VACCINATION_TEMPORARY_CONTRAINDICATION_PRECAUTION_EXPIRATION_DATE = "30944-3";
  public static final String OBSERVATION_IDENTIFIER_VACCINATION_CONTRAINDICATION_PRECAUTION = "30945-0";
  public static final String OBSERVATION_IDENTIFIER_REACTION = "31044-1";
  public static final String OBSERVATION_IDENTIFIER_DISEASE_WITH_PRESUMED_IMMUNITY = "59784-9";
  public static final String OBSERVATION_IDENTIFIER_INDICATIONS_TO_IMMUNIZE = "59785-6";
  public static final String OBSERVATION_IDENTIFIER_DOCUMENT_TYPE = "69764-9";
  public static final String OBSERVATION_IDENTIFIER_DATE_VACCINE_INFORMATION_SHEET_PUBLISHED = "29768-9";
  public static final String OBSERVATION_IDENTIFIER_DATE_VACCINE_INFORMATION_STATEMENT_PRESENTED = "29769-7";
  public static final String OBSERVATION_IDENTIFIER_VACCINES_DUE_NEXT = "30979-9";
  public static final String OBSERVATION_IDENTIFIER_DATE_VACCINE_DUE = "30980-7";
  public static final String OBSERVATION_IDENTIFIER_EARLIEST_DATE_TO_GIVE = "30981-5";
  public static final String OBSERVATION_IDENTIFIER_REASON_APPLIED_BY_FORECAST_LOGIC_TO_PROJECT_THIS_VACCINE = "30982-3";
  public static final String OBSERVATION_IDENTIFIER_IMMUNIZATION_SCHEDULE_USED = "59799-9";
  public static final String OBSERVATION_IDENTIFIER_IMMUNIZATION_SERIES_NAME = "59780-7";
  public static final String OBSERVATION_IDENTIFIER_NUMBER_OF_DOSES_IN_PRIMARY_SERIES = "59792-3";
  public static final String OBSERVATION_IDENTIFIER_DOSE_VALIDITY = "59781-5";
  public static final String OBSERVATION_IDENTIFIER_STATUS_IN_IMMUNIZATION_SERIES = "59783-1";
  public static final String OBSERVATION_IDENTIFIER_VACCINATION_TAKERESPONSE_TYPE = "46249-9";
  public static final String OBSERVATION_IDENTIFIER_VACCINATION_TAKERESPONSE_DATE = "46250-7";
  public static final String VACCINATION_FUNDING_SOURCE_PRIVATE_FUNDS = "PHC700";
  public static final String VACCINATION_FUNDING_SOURCE_FEDERAL_FUNDS = "VXC1";
  public static final String VACCINATION_FUNDING_SOURCE_STATE_FUNDS = "VXC2";
  public static final String VACCINATION_FUNDING_SOURCE_MILITARY_FUNDS = "PHC68";
  public static final String VACCINATION_FUNDING_SOURCE_TRIBAL_FUNDS = "VXC3";
  public static final String VACCINATION_FUNDING_SOURCE_OTHER = "OTH";
  public static final String VACCINATION_FUNDING_SOURCE_UNSPECIFIED = "UNK";
  public static final String CONTRAINDICATION_OR_PRECATION_ALERGY_ANAPHYLACTIC_TO_PROTEINS_OF_RODENT_OR_NEURAL_ORGIN = "VXC30";
  public static final String CONTRAINDICATION_OR_PRECATION_ALERGY_ANAPHYLACTIC_TO_2PHENOXYETHANOL = "VXC17";
  public static final String CONTRAINDICATION_OR_PRECATION_ALLERGY_TO_BAKERS_YEAST_ANAPHYLACTIC = "VXC18";
  public static final String CONTRAINDICATION_OR_PRECATION_ALERGY_TO_EGGS_DISORDER = "9193004";
  public static final String CONTRAINDICATION_OR_PRECATION_GELATIN_ALERGY_DISORDER = "294847001";
  public static final String CONTRAINDICATION_OR_PRECATION_NEOMYCIN_ALLERGY_DISORDER = "794468006";
  public static final String CONTRAINDICATION_OR_PRECATION_STREPTOMYCIN_ALLERGY_DISORDER = "294466005";
  public static final String CONTRAINDICATION_OR_PRECATION_ALLERGY_TO_THIMEROSAL_ANAPHYLACTIC = "VXC19";
  public static final String CONTRAINDICATION_OR_PRECATION_ALLERGY_TO_PREVIOUS_DOSE_OF_THIS_VACCINE_OR_TO_ANY_OF_ITS_UNLISTED_VACCINE_COMPONENTS_ANAPHYLACTIC = "VXC20";
  public static final String CONTRAINDICATION_OR_PRECATION_ALLERGY_TO_ALUMINUM_DISORDER = "402306009";
  public static final String CONTRAINDICATION_OR_PRECATION_LATEX_ALLERGY_DISORDER = "300916003";
  public static final String CONTRAINDICATION_OR_PRECATION_POLYMYXIN_B_ALLERGY_DISORDER = "294530006";
  public static final String CONTRAINDICATION_OR_PRECATION_PREVIOUS_HISOTRY_OF_INTUSSUSCEPTION = "VXC21";
  public static final String CONTRAINDICATION_OR_PRECATION_ENCEPHALOPATHY_WITHIN_7_DAYS_OF_PREVIOUS_DOSE_OF_DTP_OR_DTAP = "VXC22";
  public static final String CONTRAINDICATION_OR_PRECATION_CURRENT_FEVER_WITH_MODERATETOSEVERE_ILLNESS = "VXC23";
  public static final String CONTRAINDICATION_OR_PRECATION_CURRENT_ACUTE_ILLNESS_MODERATE_TO_SEVERE_WITH_OR_WITHOUT_FEVER = "VXC24";
  public static final String CONTRAINDICATION_OR_PRECATION_CHRONIC_DISEASE_DISORDER = "27624003";
  public static final String CONTRAINDICATION_OR_PRECATION_HISTORY_OF_ARTHUS_HYPERSENSTIVIITY_REACTION_TO_A_TETANUSCONTAINING_VACCINE_ADMINISTERED_LESS_THAN10_YEARS_PREVIOUSLY = "VXC25";
  public static final String CONTRAINDICATION_OR_PRECATION_UNDERLYING_UNSTABLE_EVOLVING_NEUROLOGIC_DIORDERS_INCLUDING_SEIZURE_DISORDERS_CEREBRAL_PALSY_AND_DEVELOPMENTAL_DELAY = "VXC26";
  public static final String CONTRAINDICATION_OR_PRECATION_IMMUNODEFICIENCY_DUE_TO_ANY_CAUSE_INCLUDING_HIV_HEMOTOLOGIC_AND_SOLID_TUMORS_CONGENTIAL_IMMUNODEFICIENCY_LONGTERM_IMMUNOSUPPRESSIVE_THERAPY_INCLUDING_STEROIDS = "VXC27";
  public static final String CONTRAINDICATION_OR_PRECATION_PATIENT_CURRENT_PREGNANT_FINDING = "77386006";
  public static final String CONTRAINDICATION_OR_PRECATION_THROMBOCYTOPENIC_DISORDER_DISORDER = "302215000";
  public static final String CONTRAINDICATION_OR_PRECATION_HISTORY_OF__PURPURA_SITUATION = "161461006";
  public static final String VACCINATION_REACTION_ANAPHYLAXIS_DISORDER = "39579001";
  public static final String VACCINATION_REACTION_DISORDER_OF_BRAIN_DISORDER = "81308009";
  public static final String VACCINATION_REACTION_PERSISTENT_INCONSOLABLE_CRYTING_LASTING_GREATER_THAN_3_HOURS_WITHIN_48_HOURS_OF_DOSE = "VXC9";
  public static final String VACCINATION_REACTION_COLLAPSE_OR_SHOCKLIKE_STATE_WITHIN_48_HOURS_OF_DOSE = "VXC10";
  public static final String VACCINATION_REACTION_CONVULSIONS_FITS_SEIZURES_WITHIN_72_HOURS_OF_DOSE = "VXC11";
  public static final String VACCINATION_REACTION_FEVER_OF_GREATER_THAN40_5C_WITHIN_48_HOURS_OF_DOSE = "VXC12";
  public static final String VACCINATION_REACTION_GUILLAINBARRE_SYNDROME_GBS_WITHIN_6_WEEKS_OF_DOSE = "VXC13";
  public static final String VACCINATION_REACTION_RASH_WITHIN_14_DAYS_OF_DOSE = "VXC14";
  public static final String VACCINATION_REACTION_INTUSSESCEPTION_WITHIN_30_DAYS_OF_DOSE = "VXC15";
  public static final String VACCINATION_SPECIAL_INDICATIONS_RABIES_EXPOSURE_WITHIN_PREVIOUS_10_DAYS = "VXC7";
  public static final String VACCINATION_SPECIAL_INDICATIONS_MEMBER_OF_SPECIAL_GROUP = "VXC8";
  public static final String FORECAST_IMMUNIZATION_SCHEDULE_ACIP_SCHEDULE = "VXC16";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_ANTHRAX_INFECTION = "409498004";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_DEPHTERIA_INFECTION = "397428000";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_TETANUS_INFECTION = "76902006";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_PERTUSSIS_INFECTION = "27836007";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_HEPATITIS_A_INFECTION = "40468003";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_HEPBATITIS_B_INFECTION = "66071002";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_HIB_INFECTION = "91428005";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_HPV_INFECTION = "240532009";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_INFLUENZA_INFECTION = "6142004";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_JAPANESE_ENCEPHALITIS_INFECTION = "52947006";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_MEALSES_INFECTION = "14189004";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_MUMPS_INFECTION = "36989005";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_RUBELLA_INFECTION = "36653000";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_MENINGOCOCCAL_INFECTION = "23511006";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_PNEUMOCOCCAL_INFECTION = "16814004";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_POLIO_INFECTION = "398102009";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_RABIES_INFECTION = "14168008";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_ROTAVIRUS_INFECTION = "18624000";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_TYPHOID_INFECTION = "4834000";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_VACCINIA_INFECTION = "111852003";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_VARICELLA_INFECTION = "38907003";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_YELLOW_FEVER_INFECTION = "16541001";
  public static final String EVIDENCE_OF_IMMUNITY_HISTORY_OF_HEPATITIS_B_INFECTION = "271511000";

  public static final String VACCINATION_VIS_DOC_TYPE_ADNOVIRUS_VIS = "253088698300001111110714";
  public static final String VACCINATION_VIS_DOC_TYPE_ANTRX_VIS = "253088698300002811100310";
  public static final String VACCINATION_VIS_DOC_TYPE_HEPATITS_A_VIS = "253088698300004211111025";
  public static final String VACCINATION_VIS_DOC_TYPE_HAEMOPHILUS_INFLUENZAE_TYPE_B_VIS = "253088698300006611981216";
  public static final String VACCINATION_VIS_DOC_TYPE_HUMAN_PAPILLOMAVIRUS_CERVARIX_VIS = "253088698300007311110503";
  public static final String VACCINATION_VIS_DOC_TYPE_HUMAN_PAPILLOMAVIRUS_GARDASIL_VIS = "253088698300008011120222";
  public static final String VACCINATION_VIS_DOC_TYPE_INFLUENZA_VACCINE__LIVE_INTRANASAL_VIS = "253088698300009711120702";
  public static final String VACCINATION_VIS_DOC_TYPE_INFLUENZA_VACCINE__INACTIVATED_VIS = "253088698300010311120702";
  public static final String VACCINATION_VIS_DOC_TYPE_JAPANESE_ENCEPHALITIS_VIS = "253088698300011011111207";
  public static final String VACCINATION_VIS_DOC_TYPE_MEASLES_MUMPS_RUBELLA_VIS = "253088698300012711120420";
  public static final String VACCINATION_VIS_DOC_TYPE_MEASLES_MUMPS_RUBELLA_VARICELLA_VIS = "253088698300013411100521";
  public static final String VACCINATION_VIS_DOC_TYPE_MENINGOCOCCAL_VIS = "253088698300014111111014";
  public static final String VACCINATION_VIS_DOC_TYPE_PNEUMOCOCCAL_CONJUGATE_PCV13_VIS = "253088698300015811100416";
  public static final String VACCINATION_VIS_DOC_TYPE_PNEUMOCOCCAL_POLYSACCHARIDE_VIS = "253088698300016511091006";
  public static final String VACCINATION_VIS_DOC_TYPE_POLIO_VIS = "253088698300017211111108";
  public static final String VACCINATION_VIS_DOC_TYPE_RABIES_VIS = "253088698300018911091006";
  public static final String VACCINATION_VIS_DOC_TYPE_SHINGLES_VIS = "253088698300020211091006";
  public static final String VACCINATION_VIS_DOC_TYPE_TETANUS_DIPHTHERIA_PERTUSSIS_VIS = "253088698300022611120124";
  public static final String VACCINATION_VIS_DOC_TYPE_TYPHOID_VIS = "253088698300023311120529";
  public static final String FINANCIAL_STATUS_OBS_METHOD_ELIGIBILITY_CAPTURED_AT_THE_IMMUNIZATION_LEVEL = "VXC40";
  public static final String FINANCIAL_STATUS_OBS_METHOD_ELIGIBILITY_CAPTURED_AT_THE_VISIT_LEVEL = "VXC41";
  public static final String VACCINATION_VIS_VACCINES_DTAP_5_PERTUSSIS_ANTIGENS = "16";
  public static final String VACCINATION_VIS_VACCINES_DTAP_IPV_HIB_HEPB = "146";
  public static final String VACCINATION_VIS_VACCINES_DTAPHEPBIPV = "110";
  public static final String VACCINATION_VIS_VACCINES_DTAPHIB = "50";
  public static final String VACCINATION_VIS_VACCINES_DTAPHIBIPV = "120";
  public static final String VACCINATION_VIS_VACCINES_DTAPIPV = "130";
  public static final String VACCINATION_VIS_VACCINES_HEP_A_ADULT = "52";
  public static final String VACCINATION_VIS_VACCINES_HEP_A_PED_ADOL_2_DOSE = "83";
  public static final String VACCINATION_VIS_VACCINES_HEP_AHEP_B = "104";
  public static final String VACCINATION_VIS_VACCINES_HEP_B_ADOLESCENT_OR_PEDIATRIC = "08";
  public static final String VACCINATION_VIS_VACCINES_HEP_B_ADOLESCENT_HIGH_RISK_INFANT = "42";
  public static final String VACCINATION_VIS_VACCINES_HEP_B_ADULT = "43";
  public static final String VACCINATION_VIS_VACCINES_HEP_B_DIALYSIS = "44";
  public static final String VACCINATION_VIS_VACCINES_HIB_PRPOMP = "49";
  public static final String VACCINATION_VIS_VACCINES_HIB_PRPT = "48";
  public static final String VACCINATION_VIS_VACCINES_HIBHEP_B = "51";
  public static final String VACCINATION_VIS_VACCINES_HPV_BIVALENT = "118";
  public static final String VACCINATION_VIS_VACCINES_HPV_QUADRIVALENT = "62";
  public static final String VACCINATION_VIS_VACCINES_INFLUENZA_HIGH_DOSE_SEASONAL = "135";
  public static final String VACCINATION_VIS_VACCINES_INFLUEZNA_LIVE_INTRANASAL = "111";
  public static final String VACCINATION_VIS_VACCINES_INFLUENZA_SEASONAL_INJECTABLE = "141";
  public static final String VACCINATION_VIS_VACCINES_INFLUENZA_SEASONAL_INJECTABLE_PRESERVATIVE_FREE = "140";
  public static final String VACCINATION_VIS_VACCINES_INFLUENZA_SEASONAL_INTRADERMAL_PRESERVATIVE_FREE = "144";
  public static final String VACCINATION_VIS_VACCINES_IPV = "10";
  public static final String VACCINATION_VIS_VACCINES_MENINGOCOCCAL_C_YHIB_PRP = "148";
  public static final String VACCINATION_VIS_VACCINES_MENINGOCOCCAL_MCVRO = "136";
  public static final String VACCINATION_VIS_VACCINES_MENINGOCOCCAL_MCV4P = "114";
  public static final String VACCINATION_VIS_VACCINES_MENINGOCOCCAL_MPSV4 = "32";
  public static final String VACCINATION_VIS_VACCINES_MMR = "03";
  public static final String VACCINATION_VIS_VACCINES_MMRV = "94";
  public static final String VACCINATION_VIS_VACCINES_PNEUMOCOCCAL_CONJUGATE_PCV_13 = "133";
  public static final String VACCINATION_VIS_VACCINES_PNEUMOCOCCAL_CONJUGATE_PCV_7 = "100";
  public static final String VACCINATION_VIS_VACCINES_ROTAVIRUS_MONOVALENT = "119";
  public static final String VACCINATION_VIS_VACCINES_ROTAVIRUS_PENTAVALENT = "116";
  public static final String VACCINATION_VIS_VACCINES_TD_ADULT = "138";
  public static final String VACCINATION_VIS_VACCINES_TD_ADULT_PRESERVATIVE_FREE = "113";
  public static final String VACCINATION_VIS_VACCINES_TD_ADULT_ADSORBED = "09";
  public static final String VACCINATION_VIS_VACCINES_TDAP = "115";
  public static final String VACCINATION_VIS_VACCINES_VARICELLA = "21";
  public static final String PATIENT_CLASS_RECURRING = "R";
  public static final String PATIENT_ETHNICITY_HISPANIC_OR_LATINO = "2135-2";
  public static final String PATIENT_ETHNICITY_NOT_HISPANIC_OR_LATINO = "2186-5";

  public static final String PATIENT_PROTECTION_NO = "N";
  public static final String PATIENT_PROTECTION_YES = "Y";
  public static final String PATIENT_PUBLICITY_NO_REMINDER_RECALL = "01";
  public static final String PATIENT_PUBLICITY_REMINDER_RECALL__ANY_METHOD = "02";
  public static final String PATIENT_PUBLICITY_REMINDER_RECALL__NO_CALLS = "03";
  public static final String PATIENT_PUBLICITY_REMINDER_ONLY__ANY_METHOD = "04";
  public static final String PATIENT_PUBLICITY_REMINDER_ONLY__NO_CALLS = "05";
  public static final String PATIENT_PUBLICITY_RECALL_ONLY__ANY_METHOD = "06";
  public static final String PATIENT_PUBLICITY_RECALL_ONLY__NO_CALLS = "07";
  public static final String PATIENT_PUBLICITY_REMINDER_RECALL__TO_PROVIDER = "08";
  public static final String PATIENT_PUBLICITY_REMINDER_TO_PROVIDER = "09";
  public static final String PATIENT_PUBLICITY_ONLY_REMINDER_TO_PROVIDER_NO_RECALL = "10";
  public static final String PATIENT_PUBLICITY_RECALL_TO_PROVIDER = "11";
  public static final String PATIENT_PUBLICITY_ONLY_RECALL_TO_PROVIDER_NO_REMINDER = "12";
  public static final String PATIENT_RACE_AMERICAN_INDIAN_OR_ALASKA_NATIVE = "1002-5";
  public static final String PATIENT_RACE_ASIAN = "2028-9";
  public static final String PATIENT_RACE_BLACK_OR_AFRICANAMERICAN = "2054-5";
  public static final String PATIENT_RACE_NATIVE_HAWAIIAN_OR_OTHER_PACIFIC_ISLANDER = "2076-8";
  public static final String PATIENT_RACE_WHITE = "2106-3";
  public static final String PATIENT_RACE_OTHER_RACE = "2131-1";

  public static final String PATIENT_SEX_FEMALE = "F";
  public static final String PATIENT_SEX_MALE = "M";

  public static final String PERSON_LANGUAGE_ARABIC = "ara";
  public static final String PERSON_LANGUAGE_ARMENIAN = "arm";
  public static final String PERSON_LANGUAGE_CATALAN_VALENCIAN = "cat";
  public static final String PERSON_LANGUAGE_CHINESE = "chi";
  public static final String PERSON_LANGUAGE_DANISH = "dan";

  public static final String PERSON_LANGUAGE_ENGLISH = "eng";
  public static final String PERSON_LANGUAGE_FRENCH = "fre";
  public static final String PERSON_LANGUAGE_GERMAN = "ger";
  public static final String PERSON_LANGUAGE_HAITIAN_HAITIAN_CREOLE = "hat";
  public static final String PERSON_LANGUAGE_HEBREW = "heb";
  public static final String PERSON_LANGUAGE_HINDI = "hin";
  public static final String PERSON_LANGUAGE_HMONG = "hmn";
  public static final String PERSON_LANGUAGE_JAPANESE = "jpn";
  public static final String PERSON_LANGUAGE_KOREAN = "kor";
  public static final String PERSON_LANGUAGE_RUSSIAN = "rus";
  public static final String PERSON_LANGUAGE_SOMALI = "som";
  public static final String PERSON_LANGUAGE_SPANISH_CASTILIAN = "spa";
  public static final String PERSON_LANGUAGE_VIETNAMESE = "vie";
  public static final String PERSON_NAME_TYPE_ALIAS_NAME = "A";
  public static final String PERSON_NAME_TYPE_NAME_AT_BIRTH = "B";
  public static final String PERSON_NAME_TYPE_ADOPTED_NAME = "C";
  public static final String PERSON_NAME_TYPE_DISPLAY_NAME = "D";
  public static final String PERSON_NAME_TYPE_LEGAL_NAME = "L";
  public static final String PERSON_NAME_TYPE_MAIDEN_NAME = "M";
  public static final String PERSON_NAME_TYPE_NAME_OF_PARTNER_SPOUSE = "P";
  public static final String PERSON_NAME_TYPE_UNSPECIFIED = "U";

  public static final String PERSON_RELATIONSHIP_CARE_GIVER = "CGV";
  public static final String PERSON_RELATIONSHIP_CHILD = "CHD";
  public static final String PERSON_RELATIONSHIP_FOSTER_CHILD = "FCH";
  public static final String PERSON_RELATIONSHIP_FATHER = "FTH";
  public static final String PERSON_RELATIONSHIP_GUARDIAN = "GRD";
  public static final String PERSON_RELATIONSHIP_GRAND_PARENT = "GRP";
  public static final String PERSON_RELATIONSHIP_MOTHER = "MTH";

  public static final String PERSON_RELATIONSHIP_PARENT = "PAR";

  public static final String REGISTRY_STATUS_ACTIVE = "A";
  public static final String REGISTRY_STATUS_INACTIVE__UNSPECIFIED = "I";
  public static final String REGISTRY_STATUS_INACTIVE__LOST_TO_FOLLOWUP = "L";
  public static final String REGISTRY_STATUS_INACTIVE__MOVED_OR_GONE_ELSEWHERE = "M";
  public static final String REGISTRY_STATUS_INACTIVE__PERMANENTLY_INACTIVE = "P";
  public static final String REGISTRY_STATUS_UNKNOWN = "U";
  public static final String TELECOMMUNICATION_EQUIPMENT_BEEPER = "BP";
  public static final String TELECOMMUNICATION_EQUIPMENT_CELLULAR_PHONE = "CP";
  public static final String TELECOMMUNICATION_EQUIPMENT_FAX = "FX";
  public static final String TELECOMMUNICATION_EQUIPMENT_INTERNET_ADDRESS = "Internet";
  public static final String TELECOMMUNICATION_EQUIPMENT_MODEM = "MD";
  public static final String TELECOMMUNICATION_EQUIPMENT_TELEPHONE = "PH";
  public static final String TELECOMMUNICATION_EQUIPMENT_TELECOMMUNICATIONS_DEVICE_FOR_THE_DEAF = "TDD";
  public static final String TELECOMMUNICATION_EQUIPMENT_TELETYPEWRITER = "TTY";
  public static final String TELECOMMUNICATION_EQUIPMENT_X_400_EMAIL_ADDRESS = "X.400";
  public static final String TELECOMMUNICATION_USE_ANSWERING_SERVICE_NUMBER = "ASN";
  public static final String TELECOMMUNICATION_USE_BEEPER_NUMBER = "BPN";
  public static final String TELECOMMUNICATION_USE_EMERGENCY_NUMBER = "EMR";
  public static final String TELECOMMUNICATION_USE_NETWORK_EMAIL_ADDRESS = "NET";
  public static final String TELECOMMUNICATION_USE_OTHER_RESIDENCE_NUMBER = "ORN";
  public static final String TELECOMMUNICATION_USE_PRIMARY_RESIDENCE_NUMBER = "PRN";
  public static final String TELECOMMUNICATION_USE_VACATION_HOME_NUMBER = "VHN";
  public static final String TELECOMMUNICATION_USE_WORK_NUMBER = "WPN";
  public static final String VACCINATION_ACTION_CODE_ADD = "A";
  public static final String VACCINATION_ACTION_CODE_DELETE = "D";
  public static final String VACCINATION_ACTION_CODE_UPDATE = "U";
  public static final String VACCINATION_COMPLETION_COMPLETE = "CP";

  public static final String VACCINATION_CONFIDENTIALITY_RESTRICTED = "R";
  public static final String VACCINATION_CONFIDENTIALITY_USUAL_CONTROL = "U";
  public static final String VACCINATION_CONFIDENTIALITY_VERY_RESTRICTED = "V";
  public static final String VACCINATION_INFORMATION_SOURCE_NEW_IMMUNIZATION_RECORD = "00";
  public static final String VACCINATION_INFORMATION_SOURCE_HISTORICAL_INFORMATION__SOURCE_UNSPECIFIED = "01";
  public static final String VACCINATION_INFORMATION_SOURCE_HISTORICAL_INFORMATION__FROM_OTHER_PROVIDER = "02";
  public static final String VACCINATION_INFORMATION_SOURCE_HISTORICAL_INFORMATION__FROM_PARENTS_WRITTEN_RECORD = "03";
  public static final String VACCINATION_INFORMATION_SOURCE_HISTORICAL_INFORMATION__FROM_PARENTS_RECALL = "04";
  public static final String VACCINATION_INFORMATION_SOURCE_HISTORICAL_INFORMATION__FROM_OTHER_REGISTRY = "05";
  public static final String VACCINATION_INFORMATION_SOURCE_HISTORICAL_INFORMATION__FROM_BIRTH_CERTIFICATE = "06";
  public static final String VACCINATION_INFORMATION_SOURCE_HISTORICAL_INFORMATION__FROM_SCHOOL_RECORD = "07";
  public static final String VACCINATION_INFORMATION_SOURCE_HISTORICAL_INFORMATION__FROM_PUBLIC_AGENCY = "08";

  public static final String VACCINATION_ORDER_CONTROL_CODE_OBSERVATIONS_TO_FOLLOW = "RE";
  public static final String VACCINATION_REFUSAL_PARENTAL_DECISION = "00";
  public static final String VACCINATION_REFUSAL_RELIGIOUS_EXEMPTION = "01";
  public static final String VACCINATION_REFUSAL_OTHER = "02";
  public static final String VACCINATION_REFUSAL_PATIENT_DECISION = "03";
  
  public static final String FACILITY_TYPE_PRIVATE = "PRI";
  public static final String FACILITY_TYPE_PUBLIC = "PUB";

  public static final String VACCINATION_VALIDITY_VALID = "V";
  public static final String VACCINATION_VALIDITY_INVALID = "I";

  public static CodeMaster getCodeMaster(CodedEntity codedEntity, CodeMaster codeMasterContext, Session session)
  {
    if (codedEntity.getTableType() != null && codedEntity.getTableType().getTableId() > 0)
    {
        List<CodeMaster> codeMasterList;
    	if (codeMasterContext == null)
    	{
        	Query query = session.createQuery("from CodeMaster where table.tableId = ? and context is null and codeValue = ?");
            query.setInteger(0, codedEntity.getTableType().getTableId());
            query.setParameter(1, codedEntity.getCode());
            codeMasterList = query.list();
    	}
    	else
    	{
        	Query query = session.createQuery("from CodeMaster where table.tableId = ? and context = ? and codeValue = ?");
            query.setInteger(0, codedEntity.getTableType().getTableId());
            query.setParameter(1, codeMasterContext);
            query.setParameter(2, codedEntity.getCode());
            codeMasterList = query.list();
    	}
      if (codeMasterList.size() > 0)
      {
        return codeMasterList.get(0);
      }
    }

    return null;
  }
}
