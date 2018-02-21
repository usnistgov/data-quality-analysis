/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.openimmunizationsoftware.dqa.InitializationException;
import org.openimmunizationsoftware.dqa.db.model.IssueAction;
import org.openimmunizationsoftware.dqa.db.model.MessageBatch;
import org.openimmunizationsoftware.dqa.db.model.MessageReceived;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssue;
import org.openimmunizationsoftware.dqa.db.model.PotentialIssueStatus;
import org.openimmunizationsoftware.dqa.nist.CompactReportModel;
import org.openimmunizationsoftware.dqa.nist.CompactReportNode;

public class PotentialIssues implements Reload
{
  public PotentialIssue GeneralAuthorizationException = null;
  public PotentialIssue GeneralConfigurationException = null;
  public PotentialIssue GeneralParseException = null;
  public PotentialIssue GeneralProcessingException = null;
  public PotentialIssue Hl7SegmentIsUnrecognized = null;
  public PotentialIssue Hl7SegmentIsInvalid = null;
  public PotentialIssue Hl7SegmentsOutOfOrder = null;
  public PotentialIssue Hl7MshAcceptAckTypeIsDeprecated = null;
  public PotentialIssue Hl7MshAcceptAckTypeIsIgnored = null;
  public PotentialIssue Hl7MshAcceptAckTypeIsInvalid = null;
  public PotentialIssue Hl7MshAcceptAckTypeIsMissing = null;
  public PotentialIssue Hl7MshAcceptAckTypeIsUnrecognized = null;
  public PotentialIssue Hl7MshAcceptAckTypeIsValuedAsAlways = null;
  public PotentialIssue Hl7MshAcceptAckTypeIsValuedAsNever = null;
  public PotentialIssue Hl7MshAcceptAckTypeIsValuedAsOnlyOnErrors = null;
  public PotentialIssue Hl7MshAltCharacterSetIsDeprecated = null;
  public PotentialIssue Hl7MshAltCharacterSetIsIgnored = null;
  public PotentialIssue Hl7MshAltCharacterSetIsInvalid = null;
  public PotentialIssue Hl7MshAltCharacterSetIsMissing = null;
  public PotentialIssue Hl7MshAltCharacterSetIsUnrecognized = null;
  public PotentialIssue Hl7MshAppAckTypeIsDeprecated = null;
  public PotentialIssue Hl7MshAppAckTypeIsIgnored = null;
  public PotentialIssue Hl7MshAppAckTypeIsInvalid = null;
  public PotentialIssue Hl7MshAppAckTypeIsMissing = null;
  public PotentialIssue Hl7MshAppAckTypeIsUnrecognized = null;
  public PotentialIssue Hl7MshAppAckTypeIsValuedAsAlways = null;
  public PotentialIssue Hl7MshAppAckTypeIsValuedAsNever = null;
  public PotentialIssue Hl7MshAppAckTypeIsValuedAsOnlyOnErrors = null;
  public PotentialIssue Hl7MshCharacterSetIsDeprecated = null;
  public PotentialIssue Hl7MshCharacterSetIsIgnored = null;
  public PotentialIssue Hl7MshCharacterSetIsInvalid = null;
  public PotentialIssue Hl7MshCharacterSetIsMissing = null;
  public PotentialIssue Hl7MshCharacterSetIsUnrecognized = null;
  public PotentialIssue Hl7MshCountryCodeIsDeprecated = null;
  public PotentialIssue Hl7MshCountryCodeIsIgnored = null;
  public PotentialIssue Hl7MshCountryCodeIsInvalid = null;
  public PotentialIssue Hl7MshCountryCodeIsMissing = null;
  public PotentialIssue Hl7MshCountryCodeIsUnrecognized = null;
  public PotentialIssue Hl7MshEncodingCharacterIsInvalid = null;
  public PotentialIssue Hl7MshEncodingCharacterIsMissing = null;
  public PotentialIssue Hl7MshEncodingCharacterIsNonStandard = null;
  public PotentialIssue Hl7MshMessageControlIdIsMissing = null;
  public PotentialIssue Hl7MshMessageDateIsInFuture = null;
  public PotentialIssue Hl7MshMessageDateIsInvalid = null;
  public PotentialIssue Hl7MshMessageDateIsMissing = null;
  public PotentialIssue Hl7MshMessageDateIsNotPrecise = null;
  public PotentialIssue Hl7MshMessageDateIsMissingTimezone = null;
  public PotentialIssue Hl7MshMessageProfileIdIsDeprecated = null;
  public PotentialIssue Hl7MshMessageProfileIdIsIgnored = null;
  public PotentialIssue Hl7MshMessageProfileIdIsInvalid = null;
  public PotentialIssue Hl7MshMessageProfileIdIsMissing = null;
  public PotentialIssue Hl7MshMessageProfileIdIsUnrecognized = null;
  public PotentialIssue Hl7MshMessageStructureIsMissing = null;
  public PotentialIssue Hl7MshMessageStructureIsUnrecognized = null;
  public PotentialIssue Hl7MshMessageTriggerIsMissing = null;
  public PotentialIssue Hl7MshMessageTriggerIsUnrecognized = null;
  public PotentialIssue Hl7MshMessageTriggerIsUnsupported = null;
  public PotentialIssue Hl7MshMessageTypeIsMissing = null;
  public PotentialIssue Hl7MshMessageTypeIsUnrecognized = null;
  public PotentialIssue Hl7MshMessageTypeIsUnsupported = null;
  public PotentialIssue Hl7MshProcessingIdIsDeprecated = null;
  public PotentialIssue Hl7MshProcessingIdIsIgnored = null;
  public PotentialIssue Hl7MshProcessingIdIsInvalid = null;
  public PotentialIssue Hl7MshProcessingIdIsMissing = null;
  public PotentialIssue Hl7MshProcessingIdIsUnrecognized = null;
  public PotentialIssue Hl7MshProcessingIdIsUnsupported = null;
  public PotentialIssue Hl7MshProcessingIdIsValuedAsDebug = null;
  public PotentialIssue Hl7MshProcessingIdIsValuedAsProduction = null;
  public PotentialIssue Hl7MshProcessingIdIsValuedAsTraining = null;
  public PotentialIssue Hl7MshReceivingApplicationIsInvalid = null;
  public PotentialIssue Hl7MshReceivingApplicationIsMissing = null;
  public PotentialIssue Hl7MshReceivingFacilityIsInvalid = null;
  public PotentialIssue Hl7MshReceivingFacilityIsMissing = null;
  public PotentialIssue Hl7MshSegmentIsMissing = null;
  public PotentialIssue Hl7MshSendingApplicationIsInvalid = null;
  public PotentialIssue Hl7MshSendingApplicationIsMissing = null;
  public PotentialIssue Hl7MshSendingFacilityIsInvalid = null;
  public PotentialIssue Hl7MshSendingFacilityIsMissing = null;
  public PotentialIssue Hl7MshVersionIsMissing = null;
  public PotentialIssue Hl7MshVersionIsUnrecognized = null;
  public PotentialIssue Hl7MshVersionIsInvalid = null;
  public PotentialIssue Hl7MshVersionIsValuedAs2_3_1 = null;
  public PotentialIssue Hl7MshVersionIsValuedAs2_4 = null;
  public PotentialIssue Hl7MshVersionIsValuedAs2_5 = null;
  public PotentialIssue Hl7Nk1SegmentIsMissing = null;
  public PotentialIssue Hl7Nk1SegmentIsRepeated = null;
  public PotentialIssue Hl7Nk1SetIdIsMissing = null;
  public PotentialIssue Hl7ObxSegmentIsMissing = null;
  public PotentialIssue Hl7OrcSegmentIsMissing = null;
  public PotentialIssue Hl7OrcSegmentIsRepeated = null;
  public PotentialIssue Hl7Pd1SegmentIsMissing = null;
  public PotentialIssue Hl7PidSegmentIsMissing = null;
  public PotentialIssue Hl7PidSegmentIsRepeated = null;
  public PotentialIssue Hl7Pv1SegmentIsMissing = null;
  public PotentialIssue Hl7Pv1SegmentIsRepeated = null;
  public PotentialIssue Hl7RxaAdminSubIdCounterIsMissing = null;
  public PotentialIssue Hl7RxaGiveSubIdIsMissing = null;
  public PotentialIssue Hl7RxaSegmentIsMissing = null;
  public PotentialIssue Hl7RxaSegmentIsRepeated = null;
  public PotentialIssue Hl7RxrSegmentIsMissing = null;
  public PotentialIssue Hl7RxrSegmentIsRepeated = null;
  public PotentialIssue NextOfKinAddressIsDifferentFromPatientAddress = null;
  public PotentialIssue NextOfKinAddressIsMissing = null;
  public PotentialIssue NextOfKinAddressCityIsInvalid = null;
  public PotentialIssue NextOfKinAddressCityIsMissing = null;
  public PotentialIssue NextOfKinAddressCityIsTooShort = null;
  public PotentialIssue NextOfKinAddressCityIsUnexpectedlyShort = null;
  public PotentialIssue NextOfKinAddressCityIsUnexpectedlyLong = null;
  public PotentialIssue NextOfKinAddressCityIsTooLong = null;
  public PotentialIssue NextOfKinAddressCountryIsDeprecated = null;
  public PotentialIssue NextOfKinAddressCountryIsIgnored = null;
  public PotentialIssue NextOfKinAddressCountryIsInvalid = null;
  public PotentialIssue NextOfKinAddressCountryIsMissing = null;
  public PotentialIssue NextOfKinAddressCountryIsUnrecognized = null;
  public PotentialIssue NextOfKinAddressCountyIsDeprecated = null;
  public PotentialIssue NextOfKinAddressCountyIsIgnored = null;
  public PotentialIssue NextOfKinAddressCountyIsInvalid = null;
  public PotentialIssue NextOfKinAddressCountyIsMissing = null;
  public PotentialIssue NextOfKinAddressCountyIsUnrecognized = null;
  public PotentialIssue NextOfKinAddressStateIsDeprecated = null;
  public PotentialIssue NextOfKinAddressStateIsIgnored = null;
  public PotentialIssue NextOfKinAddressStateIsInvalid = null;
  public PotentialIssue NextOfKinAddressStateIsMissing = null;
  public PotentialIssue NextOfKinAddressStateIsUnrecognized = null;
  public PotentialIssue NextOfKinAddressStreetIsMissing = null;
  public PotentialIssue NextOfKinAddressStreet2IsMissing = null;
  public PotentialIssue NextOfKinAddressTypeIsDeprecated = null;
  public PotentialIssue NextOfKinAddressTypeIsIgnored = null;
  public PotentialIssue NextOfKinAddressTypeIsInvalid = null;
  public PotentialIssue NextOfKinAddressTypeIsMissing = null;
  public PotentialIssue NextOfKinAddressTypeIsUnrecognized = null;
  public PotentialIssue NextOfKinAddressTypeIsValuedBadAddress = null;
  public PotentialIssue NextOfKinAddressZipIsInvalid = null;
  public PotentialIssue NextOfKinAddressZipIsMissing = null;
  public PotentialIssue NextOfKinNameIsMissing = null;
  public PotentialIssue NextOfKinNameFirstIsMissing = null;
  public PotentialIssue NextOfKinNameFirstIsTooShort = null;
  public PotentialIssue NextOfKinNameFirstIsUnexpectedlyShort = null;
  public PotentialIssue NextOfKinNameFirstIsUnexpectedlyLong = null;
  public PotentialIssue NextOfKinNameFirstIsTooLong = null;
  public PotentialIssue NextOfKinNameLastIsMissing = null;
  public PotentialIssue NextOfKinNameLastIsTooShort = null;
  public PotentialIssue NextOfKinNameLastIsUnexpectedlyShort = null;
  public PotentialIssue NextOfKinNameLastIsUnexpectedlyLong = null;
  public PotentialIssue NextOfKinNameLastIsTooLong = null;
  public PotentialIssue NextOfKinPhoneNumberIsIncomplete = null;
  public PotentialIssue NextOfKinPhoneNumberIsInvalid = null;
  public PotentialIssue NextOfKinPhoneNumberIsMissing = null;
  public PotentialIssue NextOfKinRelationshipIsDeprecated = null;
  public PotentialIssue NextOfKinRelationshipIsIgnored = null;
  public PotentialIssue NextOfKinRelationshipIsInvalid = null;
  public PotentialIssue NextOfKinRelationshipIsMissing = null;
  public PotentialIssue NextOfKinRelationshipIsNotResponsibleParty = null;
  public PotentialIssue NextOfKinRelationshipIsUnexpected = null;
  public PotentialIssue NextOfKinRelationshipIsUnrecognized = null;
  public PotentialIssue NextOfKinSsnIsMissing = null;
  public PotentialIssue ObservationValueTypeIsDeprecated = null;
  public PotentialIssue ObservationValueTypeIsIgnored = null;
  public PotentialIssue ObservationValueTypeIsInvalid = null;
  public PotentialIssue ObservationValueTypeIsMissing = null;
  public PotentialIssue ObservationValueTypeIsUnrecognized = null;
  public PotentialIssue ObservationIdentifierCodeIsDeprecated = null;
  public PotentialIssue ObservationIdentifierCodeIsIgnored = null;
  public PotentialIssue ObservationIdentifierCodeIsInvalid = null;
  public PotentialIssue ObservationIdentifierCodeIsMissing = null;
  public PotentialIssue ObservationIdentifierCodeIsUnrecognized = null;
  public PotentialIssue ObservationValueIsMissing = null;
  public PotentialIssue ObservationDateTimeOfObservationIsMissing = null;
  public PotentialIssue ObservationDateTimeOfObservationIsInvalid = null;
  public PotentialIssue PatientAddressIsMissing = null;
  public PotentialIssue PatientAddressCityIsInvalid = null;
  public PotentialIssue PatientAddressCityIsMissing = null;
  public PotentialIssue PatientAddressCityIsTooShort = null;
  public PotentialIssue PatientAddressCityIsUnexpectedlyShort = null;
  public PotentialIssue PatientAddressCityIsUnexpectedlyLong = null;
  public PotentialIssue PatientAddressCityIsTooLong = null;
  public PotentialIssue PatientAddressCountryIsDeprecated = null;
  public PotentialIssue PatientAddressCountryIsIgnored = null;
  public PotentialIssue PatientAddressCountryIsInvalid = null;
  public PotentialIssue PatientAddressCountryIsMissing = null;
  public PotentialIssue PatientAddressCountryIsUnrecognized = null;
  public PotentialIssue PatientAddressCountyIsDeprecated = null;
  public PotentialIssue PatientAddressCountyIsIgnored = null;
  public PotentialIssue PatientAddressCountyIsInvalid = null;
  public PotentialIssue PatientAddressCountyIsMissing = null;
  public PotentialIssue PatientAddressCountyIsUnrecognized = null;
  public PotentialIssue PatientAddressStateIsDeprecated = null;
  public PotentialIssue PatientAddressStateIsIgnored = null;
  public PotentialIssue PatientAddressStateIsInvalid = null;
  public PotentialIssue PatientAddressStateIsMissing = null;
  public PotentialIssue PatientAddressStateIsUnrecognized = null;
  public PotentialIssue PatientAddressStreetIsMissing = null;
  public PotentialIssue PatientAddressStreet2IsMissing = null;
  public PotentialIssue PatientAddressTypeIsMissing = null;
  public PotentialIssue PatientAddressTypeIsDeprecated = null;
  public PotentialIssue PatientAddressTypeIsIgnored = null;
  public PotentialIssue PatientAddressTypeIsInvalid = null;
  public PotentialIssue PatientAddressTypeIsUnrecognized = null;
  public PotentialIssue PatientAddressTypeIsValuedBadAddress = null;
  public PotentialIssue PatientAddressZipIsInvalid = null;
  public PotentialIssue PatientAddressZipIsMissing = null;
  public PotentialIssue PatientAliasIsMissing = null;
  public PotentialIssue PatientBirthDateIsAfterSubmission = null;
  public PotentialIssue PatientBirthDateIsInFuture = null;
  public PotentialIssue PatientBirthDateIsInvalid = null;
  public PotentialIssue PatientBirthDateIsMissing = null;
  public PotentialIssue PatientBirthDateIsUnderage = null;
  public PotentialIssue PatientBirthDateIsVeryLongAgo = null;
  public PotentialIssue PatientBirthIndicatorIsInvalid = null;
  public PotentialIssue PatientBirthIndicatorIsMissing = null;
  public PotentialIssue PatientBirthOrderIsInvalid = null;
  public PotentialIssue PatientBirthOrderIsMissing = null;
  public PotentialIssue PatientBirthOrderIsMissingAndMultipleBirthIndicated = null;
  public PotentialIssue PatientBirthPlaceIsMissing = null;
  public PotentialIssue PatientBirthPlaceIsTooShort = null;
  public PotentialIssue PatientBirthPlaceIsUnexpectedlyShort = null;
  public PotentialIssue PatientBirthPlaceIsUnexpectedlyLong = null;
  public PotentialIssue PatientBirthPlaceIsTooLong = null;
  public PotentialIssue PatientBirthRegistryIdIsInvalid = null;
  public PotentialIssue PatientBirthRegistryIdIsMissing = null;
  public PotentialIssue PatientClassIsDeprecated = null;
  public PotentialIssue PatientClassIsIgnored = null;
  public PotentialIssue PatientClassIsInvalid = null;
  public PotentialIssue PatientClassIsMissing = null;
  public PotentialIssue PatientClassIsUnrecognized = null;
  public PotentialIssue PatientDeathDateIsBeforeBirth = null;
  public PotentialIssue PatientDeathDateIsInFuture = null;
  public PotentialIssue PatientDeathDateIsInvalid = null;
  public PotentialIssue PatientDeathDateIsMissing = null;
  public PotentialIssue PatientDeathIndicatorIsInconsistent = null;
  public PotentialIssue PatientDeathIndicatorIsMissing = null;
  public PotentialIssue PatientEthnicityIsDeprecated = null;
  public PotentialIssue PatientEthnicityIsIgnored = null;
  public PotentialIssue PatientEthnicityIsInvalid = null;
  public PotentialIssue PatientEthnicityIsMissing = null;
  public PotentialIssue PatientEthnicityIsUnrecognized = null;
  public PotentialIssue PatientGenderIsDeprecated = null;
  public PotentialIssue PatientGenderIsIgnored = null;
  public PotentialIssue PatientGenderIsInvalid = null;
  public PotentialIssue PatientGenderIsMissing = null;
  public PotentialIssue PatientGenderIsUnrecognized = null;
  public PotentialIssue PatientGuardianAddressIsMissing = null;
  public PotentialIssue PatientGuardianAddressCityIsMissing = null;
  public PotentialIssue PatientGuardianAddressStateIsMissing = null;
  public PotentialIssue PatientGuardianAddressStreetIsMissing = null;
  public PotentialIssue PatientGuardianAddressZipIsMissing = null;
  public PotentialIssue PatientGuardianNameIsMissing = null;
  public PotentialIssue PatientGuardianNameIsSameAsUnderagePatient = null;
  public PotentialIssue PatientGuardianNameHasJunkName = null;
  public PotentialIssue PatientGuardianNameFirstIsMissing = null;
  public PotentialIssue PatientGuardianNameLastIsMissing = null;
  public PotentialIssue PatientGuardianResponsiblePartyIsMissing = null;
  public PotentialIssue PatientGuardianPhoneIsMissing = null;
  public PotentialIssue PatientGuardianRelationshipIsMissing = null;
  public PotentialIssue PatientImmunityCodeIsDeprecated = null;
  public PotentialIssue PatientImmunityCodeIsIgnored = null;
  public PotentialIssue PatientImmunityCodeIsInvalid = null;
  public PotentialIssue PatientImmunityCodeIsMissing = null;
  public PotentialIssue PatientImmunityCodeIsUnrecognized = null;
  public PotentialIssue PatientImmunizationRegistryStatusIsDeprecated = null;
  public PotentialIssue PatientImmunizationRegistryStatusIsIgnored = null;
  public PotentialIssue PatientImmunizationRegistryStatusIsInvalid = null;
  public PotentialIssue PatientImmunizationRegistryStatusIsMissing = null;
  public PotentialIssue PatientImmunizationRegistryStatusIsUnrecognized = null;
  public PotentialIssue PatientMedicaidNumberIsInvalid = null;
  public PotentialIssue PatientMedicaidNumberIsMissing = null;
  public PotentialIssue PatientMiddleNameIsMissing = null;
  public PotentialIssue PatientMiddleNameIsInvalid = null;
  public PotentialIssue PatientMiddleNameMayBeInitial = null;
  public PotentialIssue PatientMiddleNameIsTooShort = null;
  public PotentialIssue PatientMiddleNameIsUnexpectedlyShort = null;
  public PotentialIssue PatientMiddleNameIsUnexpectedlyLong = null;
  public PotentialIssue PatientMiddleNameIsTooLong = null;
  public PotentialIssue PatientMotherSMaidenNameIsInvalid = null;
  public PotentialIssue PatientMotherSMaidenNameHasJunkName = null;
  public PotentialIssue PatientMotherSMaidenNameHasInvalidPrefixes = null;
  public PotentialIssue PatientMotherSMaidenNameIsMissing = null;
  public PotentialIssue PatientMotherSMaidenNameIsTooShort = null;
  public PotentialIssue PatientMotherSMaidenNameIsUnexpectedlyShort = null;
  public PotentialIssue PatientMotherSMaidenNameIsUnexpectedlyLong = null;
  public PotentialIssue PatientMotherSMaidenNameIsTooLong = null;
  public PotentialIssue PatientNameMayBeTemporaryNewbornName = null;
  public PotentialIssue PatientNameMayBeTestName = null;
  public PotentialIssue PatientNameHasJunkName = null;
  public PotentialIssue PatientNameIsAKnownTestName = null;
  public PotentialIssue PatientNameFirstIsInvalid = null;
  public PotentialIssue PatientNameFirstIsMissing = null;
  public PotentialIssue PatientNameFirstIsTooShort = null;
  public PotentialIssue PatientNameFirstIsUnexpectedlyShort = null;
  public PotentialIssue PatientNameFirstIsUnexpectedlyLong = null;
  public PotentialIssue PatientNameFirstIsTooLong = null;
  public PotentialIssue PatientNameFirstMayIncludeMiddleInitial = null;
  public PotentialIssue PatientNameLastIsInvalid = null;
  public PotentialIssue PatientNameLastIsMissing = null;
  public PotentialIssue PatientNameLastIsTooShort = null;
  public PotentialIssue PatientNameLastIsUnexpectedlyShort = null;
  public PotentialIssue PatientNameLastIsUnexpectedlyLong = null;
  public PotentialIssue PatientNameLastIsTooLong = null;
  public PotentialIssue PatientNameTypeCodeIsDeprecated = null;
  public PotentialIssue PatientNameTypeCodeIsIgnored = null;
  public PotentialIssue PatientNameTypeCodeIsInvalid = null;
  public PotentialIssue PatientNameTypeCodeIsMissing = null;
  public PotentialIssue PatientNameTypeCodeIsUnrecognized = null;
  public PotentialIssue PatientNameTypeCodeIsNotValuedLegal = null;
  public PotentialIssue PatientPhoneIsIncomplete = null;
  public PotentialIssue PatientPhoneIsInvalid = null;
  public PotentialIssue PatientPhoneIsMissing = null;
  public PotentialIssue PatientPhoneTelUseCodeIsDeprecated = null;
  public PotentialIssue PatientPhoneTelUseCodeIsIgnored = null;
  public PotentialIssue PatientPhoneTelUseCodeIsInvalid = null;
  public PotentialIssue PatientPhoneTelUseCodeIsMissing = null;
  public PotentialIssue PatientPhoneTelUseCodeIsUnrecognized = null;
  public PotentialIssue PatientPhoneTelEquipCodeIsDeprecated = null;
  public PotentialIssue PatientPhoneTelEquipCodeIsIgnored = null;
  public PotentialIssue PatientPhoneTelEquipCodeIsInvalid = null;
  public PotentialIssue PatientPhoneTelEquipCodeIsMissing = null;
  public PotentialIssue PatientPhoneTelEquipCodeIsUnrecognized = null;
  public PotentialIssue PatientPrimaryFacilityIdIsDeprecated = null;
  public PotentialIssue PatientPrimaryFacilityIdIsIgnored = null;
  public PotentialIssue PatientPrimaryFacilityIdIsInvalid = null;
  public PotentialIssue PatientPrimaryFacilityIdIsMissing = null;
  public PotentialIssue PatientPrimaryFacilityIdIsUnrecognized = null;
  public PotentialIssue PatientPrimaryFacilityNameIsMissing = null;
  public PotentialIssue PatientPrimaryLanguageIsDeprecated = null;
  public PotentialIssue PatientPrimaryLanguageIsIgnored = null;
  public PotentialIssue PatientPrimaryLanguageIsInvalid = null;
  public PotentialIssue PatientPrimaryLanguageIsMissing = null;
  public PotentialIssue PatientPrimaryLanguageIsUnrecognized = null;
  public PotentialIssue PatientPrimaryPhysicianIdIsDeprecated = null;
  public PotentialIssue PatientPrimaryPhysicianIdIsIgnored = null;
  public PotentialIssue PatientPrimaryPhysicianIdIsInvalid = null;
  public PotentialIssue PatientPrimaryPhysicianIdIsMissing = null;
  public PotentialIssue PatientPrimaryPhysicianIdIsUnrecognized = null;
  public PotentialIssue PatientPrimaryPhysicianNameIsMissing = null;
  public PotentialIssue PatientProtectionIndicatorIsDeprecated = null;
  public PotentialIssue PatientProtectionIndicatorIsIgnored = null;
  public PotentialIssue PatientProtectionIndicatorIsInvalid = null;
  public PotentialIssue PatientProtectionIndicatorIsMissing = null;
  public PotentialIssue PatientProtectionIndicatorIsUnrecognized = null;
  public PotentialIssue PatientProtectionIndicatorIsValuedAsNo = null;
  public PotentialIssue PatientProtectionIndicatorIsValuedAsYes = null;
  public PotentialIssue PatientPublicityCodeIsDeprecated = null;
  public PotentialIssue PatientPublicityCodeIsIgnored = null;
  public PotentialIssue PatientPublicityCodeIsInvalid = null;
  public PotentialIssue PatientPublicityCodeIsMissing = null;
  public PotentialIssue PatientPublicityCodeIsUnrecognized = null;
  public PotentialIssue PatientRaceIsDeprecated = null;
  public PotentialIssue PatientRaceIsIgnored = null;
  public PotentialIssue PatientRaceIsInvalid = null;
  public PotentialIssue PatientRaceIsMissing = null;
  public PotentialIssue PatientRaceIsUnrecognized = null;
  public PotentialIssue PatientRegistryIdIsMissing = null;
  public PotentialIssue PatientRegistryIdIsUnrecognized = null;
  public PotentialIssue PatientRegistryStatusIsDeprecated = null;
  public PotentialIssue PatientRegistryStatusIsIgnored = null;
  public PotentialIssue PatientRegistryStatusIsInvalid = null;
  public PotentialIssue PatientRegistryStatusIsMissing = null;
  public PotentialIssue PatientRegistryStatusIsUnrecognized = null;
  public PotentialIssue PatientSsnIsInvalid = null;
  public PotentialIssue PatientSsnIsMissing = null;
  public PotentialIssue PatientSubmitterIdIsMissing = null;
  public PotentialIssue PatientSubmitterIdAuthorityIsMissing = null;
  public PotentialIssue PatientSubmitterIdTypeCodeIsMissing = null;
  public PotentialIssue PatientSubmitterIdTypeCodeIsDeprecated = null;
  public PotentialIssue PatientSubmitterIdTypeCodeIsInvalid = null;
  public PotentialIssue PatientSubmitterIdTypeCodeIsUnrecognized = null;
  public PotentialIssue PatientSubmitterIdTypeCodeIsIgnored = null;
  public PotentialIssue PatientSystemCreationDateIsMissing = null;
  public PotentialIssue PatientSystemCreationDateIsInvalid = null;
  public PotentialIssue PatientSystemCreationDateIsBeforeBirth = null;
  public PotentialIssue PatientSystemCreationDateIsInFuture = null;
  public PotentialIssue PatientVfcEffectiveDateIsBeforeBirth = null;
  public PotentialIssue PatientVfcEffectiveDateIsInFuture = null;
  public PotentialIssue PatientVfcEffectiveDateIsInvalid = null;
  public PotentialIssue PatientVfcEffectiveDateIsMissing = null;
  public PotentialIssue PatientVfcStatusIsDeprecated = null;
  public PotentialIssue PatientVfcStatusIsIgnored = null;
  public PotentialIssue PatientVfcStatusIsInvalid = null;
  public PotentialIssue PatientVfcStatusIsMissing = null;
  public PotentialIssue PatientVfcStatusIsUnrecognized = null;
  public PotentialIssue PatientWicIdIsInvalid = null;
  public PotentialIssue PatientWicIdIsMissing = null;
  public PotentialIssue VaccinationActionCodeIsDeprecated = null;
  public PotentialIssue VaccinationActionCodeIsIgnored = null;
  public PotentialIssue VaccinationActionCodeIsInvalid = null;
  public PotentialIssue VaccinationActionCodeIsMissing = null;
  public PotentialIssue VaccinationActionCodeIsUnrecognized = null;
  public PotentialIssue VaccinationActionCodeIsValuedAsAdd = null;
  public PotentialIssue VaccinationActionCodeIsValuedAsAddOrUpdate = null;
  public PotentialIssue VaccinationActionCodeIsValuedAsDelete = null;
  public PotentialIssue VaccinationActionCodeIsValuedAsUpdate = null;
  public PotentialIssue VaccinationAdminCodeIsDeprecated = null;
  public PotentialIssue VaccinationAdminCodeIsIgnored = null;
  public PotentialIssue VaccinationAdminCodeIsInvalid = null;
  public PotentialIssue VaccinationAdminCodeIsInvalidForDateAdministered = null;
  public PotentialIssue VaccinationAdminCodeIsMissing = null;
  public PotentialIssue VaccinationAdminCodeIsNotSpecific = null;
  public PotentialIssue VaccinationAdminCodeIsNotVaccine = null;
  public PotentialIssue VaccinationAdminCodeIsUnexpectedForDateAdministered = null;
  public PotentialIssue VaccinationAdminCodeIsUnrecognized = null;
  public PotentialIssue VaccinationAdminCodeIsValuedAsNotAdministered = null;
  public PotentialIssue VaccinationAdminCodeIsValuedAsUnknown = null;
  public PotentialIssue VaccinationAdminCodeMayBeVariationOfPreviouslyReportedCodes = null;
  public PotentialIssue VaccinationAdminCodeTableIsMissing = null;
  public PotentialIssue VaccinationAdminCodeTableIsInvalid = null;
  public PotentialIssue VaccinationAdminDateIsAfterLotExpirationDate = null;
  public PotentialIssue VaccinationAdminDateIsAfterMessageSubmitted = null;
  public PotentialIssue VaccinationAdminDateIsAfterPatientDeathDate = null;
  public PotentialIssue VaccinationAdminDateIsAfterSystemEntryDate = null;
  public PotentialIssue VaccinationAdminDateIsBeforeBirth = null;
  public PotentialIssue VaccinationAdminDateIsBeforeOrAfterExpectedVaccineUsageRange = null;
  public PotentialIssue VaccinationAdminDateIsBeforeOrAfterLicensedVaccineRange = null;
  public PotentialIssue VaccinationAdminDateIsBeforeOrAfterWhenExpectedForPatientAge = null;
  public PotentialIssue VaccinationAdminDateIsBeforeOrAfterWhenValidForPatientAge = null;
  public PotentialIssue VaccinationAdminDateIsInvalid = null;
  public PotentialIssue VaccinationAdminDateIsMissing = null;
  public PotentialIssue VaccinationAdminDateIsOn15ThDayOfMonth = null;
  public PotentialIssue VaccinationAdminDateIsOnFirstDayOfMonth = null;
  public PotentialIssue VaccinationAdminDateIsOnLastDayOfMonth = null;
  public PotentialIssue VaccinationAdminDateIsReportedLate = null;
  public PotentialIssue VaccinationAdminDateEndIsDifferentFromStartDate = null;
  public PotentialIssue VaccinationAdminDateEndIsMissing = null;
  public PotentialIssue VaccinationAdministeredAmountIsInvalid = null;
  public PotentialIssue VaccinationAdministeredAmountIsMissing = null;
  public PotentialIssue VaccinationAdministeredAmountIsValuedAsZero = null;
  public PotentialIssue VaccinationAdministeredAmountIsValuedAsUnknown = null;
  public PotentialIssue VaccinationAdministeredUnitIsDeprecated = null;
  public PotentialIssue VaccinationAdministeredUnitIsIgnored = null;
  public PotentialIssue VaccinationAdministeredUnitIsInvalid = null;
  public PotentialIssue VaccinationAdministeredUnitIsMissing = null;
  public PotentialIssue VaccinationAdministeredUnitIsUnrecognized = null;
  public PotentialIssue VaccinationBodyRouteIsDeprecated = null;
  public PotentialIssue VaccinationBodyRouteIsIgnored = null;
  public PotentialIssue VaccinationBodyRouteIsInvalid = null;
  public PotentialIssue VaccinationBodyRouteIsInvalidForVaccineIndicated = null;
  public PotentialIssue VaccinationBodyRouteIsInvalidForBodySiteIndicated = null;
  public PotentialIssue VaccinationBodyRouteIsMissing = null;
  public PotentialIssue VaccinationBodyRouteIsUnrecognized = null;
  public PotentialIssue VaccinationBodySiteIsDeprecated = null;
  public PotentialIssue VaccinationBodySiteIsIgnored = null;
  public PotentialIssue VaccinationBodySiteIsInvalid = null;
  public PotentialIssue VaccinationBodySiteIsInvalidForVaccineIndicated = null;
  public PotentialIssue VaccinationBodySiteIsMissing = null;
  public PotentialIssue VaccinationBodySiteIsUnrecognized = null;
  public PotentialIssue VaccinationCompletionStatusIsDeprecated = null;
  public PotentialIssue VaccinationCompletionStatusIsIgnored = null;
  public PotentialIssue VaccinationCompletionStatusIsInvalid = null;
  public PotentialIssue VaccinationCompletionStatusIsMissing = null;
  public PotentialIssue VaccinationCompletionStatusIsUnrecognized = null;
  public PotentialIssue VaccinationCompletionStatusIsValuedAsCompleted = null;
  public PotentialIssue VaccinationCompletionStatusIsValuedAsNotAdministered = null;
  public PotentialIssue VaccinationCompletionStatusIsValuedAsPartiallyAdministered = null;
  public PotentialIssue VaccinationCompletionStatusIsValuedAsRefused = null;
  public PotentialIssue VaccinationConfidentialityCodeIsDeprecated = null;
  public PotentialIssue VaccinationConfidentialityCodeIsIgnored = null;
  public PotentialIssue VaccinationConfidentialityCodeIsInvalid = null;
  public PotentialIssue VaccinationConfidentialityCodeIsMissing = null;
  public PotentialIssue VaccinationConfidentialityCodeIsUnrecognized = null;
  public PotentialIssue VaccinationConfidentialityCodeIsValuedAsRestricted = null;
  public PotentialIssue VaccinationCptCodeIsDeprecated = null;
  public PotentialIssue VaccinationCptCodeIsIgnored = null;
  public PotentialIssue VaccinationCptCodeIsInvalid = null;
  public PotentialIssue VaccinationCptCodeIsInvalidForDateAdministered = null;
  public PotentialIssue VaccinationCptCodeIsMissing = null;
  public PotentialIssue VaccinationCptCodeIsUnexpectedForDateAdministered = null;
  public PotentialIssue VaccinationCptCodeIsUnrecognized = null;
  public PotentialIssue VaccinationCvxCodeIsDeprecated = null;
  public PotentialIssue VaccinationCvxCodeIsIgnored = null;
  public PotentialIssue VaccinationCvxCodeIsInvalid = null;
  public PotentialIssue VaccinationCvxCodeIsInvalidForDateAdministered = null;
  public PotentialIssue VaccinationCvxCodeIsMissing = null;
  public PotentialIssue VaccinationCvxCodeIsUnexpectedForDateAdministered = null;
  public PotentialIssue VaccinationCvxCodeIsUnrecognized = null;
  public PotentialIssue VaccinationCvxCodeAndCptCodeAreInconsistent = null;
  public PotentialIssue VaccinationFacilityIdIsDeprecated = null;
  public PotentialIssue VaccinationFacilityIdIsIgnored = null;
  public PotentialIssue VaccinationFacilityIdIsInvalid = null;
  public PotentialIssue VaccinationFacilityIdIsMissing = null;
  public PotentialIssue VaccinationFacilityIdIsUnrecognized = null;
  public PotentialIssue VaccinationFacilityNameIsMissing = null;
  public PotentialIssue VaccinationFacilityTypeIsDeprecated = null;
  public PotentialIssue VaccinationFacilityTypeIsIgnored = null;
  public PotentialIssue VaccinationFacilityTypeIsInvalid = null;
  public PotentialIssue VaccinationFacilityTypeIsMissing = null;
  public PotentialIssue VaccinationFacilityTypeIsUnrecognized = null;
  public PotentialIssue VaccinationFacilityTypeIsValuedAsPublic = null;
  public PotentialIssue VaccinationFacilityTypeIsValuedAsPrivate = null;
  public PotentialIssue VaccinationFillerOrderNumberIsDeprecated = null;
  public PotentialIssue VaccinationFillerOrderNumberIsIgnored = null;
  public PotentialIssue VaccinationFillerOrderNumberIsInvalid = null;
  public PotentialIssue VaccinationFillerOrderNumberIsMissing = null;
  public PotentialIssue VaccinationFillerOrderNumberIsUnrecognized = null;
  public PotentialIssue VaccinationFinancialEligibilityCodeIsDeprecated = null;
  public PotentialIssue VaccinationFinancialEligibilityCodeIsIgnored = null;
  public PotentialIssue VaccinationFinancialEligibilityCodeIsInvalid = null;
  public PotentialIssue VaccinationFinancialEligibilityCodeIsMissing = null;
  public PotentialIssue VaccinationFinancialEligibilityCodeIsUnrecognized = null;
  public PotentialIssue VaccinationGivenByIsDeprecated = null;
  public PotentialIssue VaccinationGivenByIsIgnored = null;
  public PotentialIssue VaccinationGivenByIsInvalid = null;
  public PotentialIssue VaccinationGivenByIsMissing = null;
  public PotentialIssue VaccinationGivenByIsUnrecognized = null;
  public PotentialIssue VaccinationIdIsMissing = null;
  public PotentialIssue VaccinationIdOfReceiverIsMissing = null;
  public PotentialIssue VaccinationIdOfReceiverIsUnrecognized = null;
  public PotentialIssue VaccinationIdOfSenderIsMissing = null;
  public PotentialIssue VaccinationIdOfSenderIsUnrecognized = null;
  public PotentialIssue VaccinationInformationSourceIsAdministeredButAppearsToHistorical = null;
  public PotentialIssue VaccinationInformationSourceIsDeprecated = null;
  public PotentialIssue VaccinationInformationSourceIsHistoricalButAppearsToBeAdministered = null;
  public PotentialIssue VaccinationInformationSourceIsIgnored = null;
  public PotentialIssue VaccinationInformationSourceIsInvalid = null;
  public PotentialIssue VaccinationInformationSourceIsMissing = null;
  public PotentialIssue VaccinationInformationSourceIsUnrecognized = null;
  public PotentialIssue VaccinationInformationSourceIsValuedAsAdministered = null;
  public PotentialIssue VaccinationInformationSourceIsValuedAsHistorical = null;
  public PotentialIssue VaccinationVisIsMissing = null;
  public PotentialIssue VaccinationVisIsUnrecognized = null;
  public PotentialIssue VaccinationVisIsDeprecated = null;
  public PotentialIssue VaccinationVisCvxCodeIsDeprecated = null;
  public PotentialIssue VaccinationVisCvxCodeIsIgnored = null;
  public PotentialIssue VaccinationVisCvxCodeIsInvalid = null;
  public PotentialIssue VaccinationVisCvxCodeIsMissing = null;
  public PotentialIssue VaccinationVisCvxCodeIsUnrecognized = null;
  public PotentialIssue VaccinationVisDocumentTypeIsDeprecated = null;
  public PotentialIssue VaccinationVisDocumentTypeIsIgnored = null;
  public PotentialIssue VaccinationVisDocumentTypeIsIncorrect = null;
  public PotentialIssue VaccinationVisDocumentTypeIsInvalid = null;
  public PotentialIssue VaccinationVisDocumentTypeIsMissing = null;
  public PotentialIssue VaccinationVisDocumentTypeIsUnrecognized = null;
  public PotentialIssue VaccinationVisDocumentTypeIsOutOfDate = null;
  public PotentialIssue VaccinationVisPublishedDateIsInvalid = null;
  public PotentialIssue VaccinationVisPublishedDateIsMissing = null;
  public PotentialIssue VaccinationVisPublishedDateIsUnrecognized = null;
  public PotentialIssue VaccinationVisPublishedDateIsInFuture = null;
  public PotentialIssue VaccinationVisPresentedDateIsInvalid = null;
  public PotentialIssue VaccinationVisPresentedDateIsMissing = null;
  public PotentialIssue VaccinationVisPresentedDateIsNotAdminDate = null;
  public PotentialIssue VaccinationVisPresentedDateIsBeforePublishedDate = null;
  public PotentialIssue VaccinationVisPresentedDateIsAfterAdminDate = null;
  public PotentialIssue VaccinationLotExpirationDateIsInvalid = null;
  public PotentialIssue VaccinationLotExpirationDateIsMissing = null;
  public PotentialIssue VaccinationLotNumberIsInvalid = null;
  public PotentialIssue VaccinationLotNumberIsMissing = null;
  public PotentialIssue VaccinationManufacturerCodeIsDeprecated = null;
  public PotentialIssue VaccinationManufacturerCodeIsIgnored = null;
  public PotentialIssue VaccinationManufacturerCodeIsInvalid = null;
  public PotentialIssue VaccinationManufacturerCodeIsInvalidForDateAdministered = null;
  public PotentialIssue VaccinationManufacturerCodeIsMissing = null;
  public PotentialIssue VaccinationManufacturerCodeIsUnexpectedForDateAdministered = null;
  public PotentialIssue VaccinationManufacturerCodeIsUnrecognized = null;
  public PotentialIssue VaccinationOrderControlCodeIsDeprecated = null;
  public PotentialIssue VaccinationOrderControlCodeIsIgnored = null;
  public PotentialIssue VaccinationOrderControlCodeIsInvalid = null;
  public PotentialIssue VaccinationOrderControlCodeIsMissing = null;
  public PotentialIssue VaccinationOrderControlCodeIsUnrecognized = null;
  public PotentialIssue VaccinationOrderFacilityIdIsDeprecated = null;
  public PotentialIssue VaccinationOrderFacilityIdIsIgnored = null;
  public PotentialIssue VaccinationOrderFacilityIdIsInvalid = null;
  public PotentialIssue VaccinationOrderFacilityIdIsMissing = null;
  public PotentialIssue VaccinationOrderFacilityIdIsUnrecognized = null;
  public PotentialIssue VaccinationOrderFacilityNameIsMissing = null;
  public PotentialIssue VaccinationOrderedByIsDeprecated = null;
  public PotentialIssue VaccinationOrderedByIsIgnored = null;
  public PotentialIssue VaccinationOrderedByIsInvalid = null;
  public PotentialIssue VaccinationOrderedByIsMissing = null;
  public PotentialIssue VaccinationOrderedByIsUnrecognized = null;
  public PotentialIssue VaccinationPlacerOrderNumberIsDeprecated = null;
  public PotentialIssue VaccinationPlacerOrderNumberIsIgnored = null;
  public PotentialIssue VaccinationPlacerOrderNumberIsInvalid = null;
  public PotentialIssue VaccinationPlacerOrderNumberIsMissing = null;
  public PotentialIssue VaccinationPlacerOrderNumberIsUnrecognized = null;
  public PotentialIssue VaccinationProductIsDeprecated = null;
  public PotentialIssue VaccinationProductIsInvalid = null;
  public PotentialIssue VaccinationProductIsInvalidForDateAdministered = null;
  public PotentialIssue VaccinationProductIsMissing = null;
  public PotentialIssue VaccinationProductIsUnexpectedForDateAdministered = null;
  public PotentialIssue VaccinationProductIsUnrecognized = null;
  public PotentialIssue VaccinationRecordedByIsDeprecated = null;
  public PotentialIssue VaccinationRecordedByIsIgnored = null;
  public PotentialIssue VaccinationRecordedByIsInvalid = null;
  public PotentialIssue VaccinationRecordedByIsMissing = null;
  public PotentialIssue VaccinationRecordedByIsUnrecognized = null;
  public PotentialIssue VaccinationRefusalReasonConflictsCompletionStatus = null;
  public PotentialIssue VaccinationRefusalReasonIsDeprecated = null;
  public PotentialIssue VaccinationRefusalReasonIsIgnored = null;
  public PotentialIssue VaccinationRefusalReasonIsInvalid = null;
  public PotentialIssue VaccinationRefusalReasonIsMissing = null;
  public PotentialIssue VaccinationRefusalReasonIsUnrecognized = null;
  public PotentialIssue VaccinationSystemEntryTimeIsInFuture = null;
  public PotentialIssue VaccinationSystemEntryTimeIsInvalid = null;
  public PotentialIssue VaccinationSystemEntryTimeIsMissing = null;
  public PotentialIssue VaccinationTradeNameIsDeprecated = null;
  public PotentialIssue VaccinationTradeNameIsIgnored = null;
  public PotentialIssue VaccinationTradeNameIsInvalid = null;
  public PotentialIssue VaccinationTradeNameIsMissing = null;
  public PotentialIssue VaccinationTradeNameIsUnrecognized = null;
  public PotentialIssue VaccinationTradeNameAndVaccineAreInconsistent = null;
  public PotentialIssue VaccinationTradeNameAndManufacturerAreInconsistent = null;
  public PotentialIssue VaccinationValidityCodeIsInvalid = null;
  public PotentialIssue VaccinationValidityCodeIsDeprecated = null;
  public PotentialIssue VaccinationValidityCodeIsIgnored = null;
  public PotentialIssue VaccinationValidityCodeIsMissing = null;
  public PotentialIssue VaccinationValidityCodeIsUnrecognized = null;
  public PotentialIssue VaccinationValidityCodeIsValuedAsValid = null;
  public PotentialIssue VaccinationValidityCodeIsValuedAsInvalid = null;

  public static enum Field {
    GENERAL_AUTHORIZATION, GENERAL_CONFIGURATION, GENERAL_PARSE, GENERAL_PROCESSING, HL7_SEGMENT, HL7_SEGMENTS, HL7_MSH_ACCEPT_ACK_TYPE, HL7_MSH_ALT_CHARACTER_SET, HL7_MSH_APP_ACK_TYPE, HL7_MSH_CHARACTER_SET, HL7_MSH_COUNTRY_CODE, HL7_MSH_ENCODING_CHARACTER, HL7_MSH_MESSAGE_CONTROL_ID, HL7_MSH_MESSAGE_DATE, HL7_MSH_MESSAGE_PROFILE_ID, HL7_MSH_MESSAGE_STRUCTURE, HL7_MSH_MESSAGE_TRIGGER, HL7_MSH_MESSAGE_TYPE, HL7_MSH_PROCESSING_ID, HL7_MSH_RECEIVING_APPLICATION, HL7_MSH_RECEIVING_FACILITY, HL7_MSH_SEGMENT, HL7_MSH_SENDING_APPLICATION, HL7_MSH_SENDING_FACILITY, HL7_MSH_VERSION, HL7_NK1_SEGMENT, HL7_NK1_SET_ID, HL7_OBX_SEGMENT, HL7_ORC_SEGMENT, HL7_PD1_SEGMENT, HL7_PID_SEGMENT, HL7_PV1_SEGMENT, HL7_RXA_ADMIN_SUB_ID_COUNTER, HL7_RXA_GIVE_SUB_ID, HL7_RXA_SEGMENT, HL7_RXR_SEGMENT, NEXT_OF_KIN_ADDRESS, NEXT_OF_KIN_ADDRESS_CITY, NEXT_OF_KIN_ADDRESS_COUNTRY, NEXT_OF_KIN_ADDRESS_COUNTY, NEXT_OF_KIN_ADDRESS_STATE, NEXT_OF_KIN_ADDRESS_STREET, NEXT_OF_KIN_ADDRESS_STREET2, NEXT_OF_KIN_ADDRESS_TYPE, NEXT_OF_KIN_ADDRESS_ZIP, NEXT_OF_KIN_NAME, NEXT_OF_KIN_NAME_FIRST, NEXT_OF_KIN_NAME_LAST, NEXT_OF_KIN_PHONE_NUMBER, NEXT_OF_KIN_RELATIONSHIP, NEXT_OF_KIN_SSN, OBSERVATION_VALUE_TYPE, OBSERVATION_IDENTIFIER_CODE, OBSERVATION_VALUE, OBSERVATION_DATE_TIME_OF_OBSERVATION, PATIENT_ADDRESS, PATIENT_ADDRESS_CITY, PATIENT_ADDRESS_COUNTRY, PATIENT_ADDRESS_COUNTY, PATIENT_ADDRESS_STATE, PATIENT_ADDRESS_STREET, PATIENT_ADDRESS_STREET2, PATIENT_ADDRESS_TYPE, PATIENT_ADDRESS_ZIP, PATIENT_ALIAS, PATIENT_BIRTH_DATE, PATIENT_BIRTH_INDICATOR, PATIENT_BIRTH_ORDER, PATIENT_BIRTH_PLACE, PATIENT_BIRTH_REGISTRY_ID, PATIENT_CLASS, PATIENT_DEATH_DATE, PATIENT_DEATH_INDICATOR, PATIENT_ETHNICITY, PATIENT_GENDER, PATIENT_GUARDIAN_ADDRESS, PATIENT_GUARDIAN_ADDRESS_CITY, PATIENT_GUARDIAN_ADDRESS_STATE, PATIENT_GUARDIAN_ADDRESS_STREET, PATIENT_GUARDIAN_ADDRESS_ZIP, PATIENT_GUARDIAN_NAME, PATIENT_GUARDIAN_NAME_FIRST, PATIENT_GUARDIAN_NAME_LAST, PATIENT_GUARDIAN_RESPONSIBLE_PARTY, PATIENT_GUARDIAN_PHONE, PATIENT_GUARDIAN_RELATIONSHIP, PATIENT_IMMUNITY_CODE, PATIENT_IMMUNIZATION_REGISTRY_STATUS, PATIENT_MEDICAID_NUMBER, PATIENT_MIDDLE_NAME, PATIENT_MOTHERS_MAIDEN_NAME, PATIENT_NAME, PATIENT_NAME_FIRST, PATIENT_NAME_LAST, PATIENT_NAME_TYPE_CODE, PATIENT_PHONE, PATIENT_PHONE_TEL_USE_CODE, PATIENT_PHONE_TEL_EQUIP_CODE, PATIENT_PRIMARY_FACILITY_ID, PATIENT_PRIMARY_FACILITY_NAME, PATIENT_PRIMARY_LANGUAGE, PATIENT_PRIMARY_PHYSICIAN_ID, PATIENT_PRIMARY_PHYSICIAN_NAME, PATIENT_PROTECTION_INDICATOR, PATIENT_PUBLICITY_CODE, PATIENT_RACE, PATIENT_REGISTRY_ID, PATIENT_REGISTRY_STATUS, PATIENT_SSN, PATIENT_SUBMITTER_ID, PATIENT_SUBMITTER_ID_AUTHORITY, PATIENT_SUBMITTER_ID_TYPE_CODE, PATIENT_SYSTEM_CREATION_DATE, PATIENT_VFC_EFFECTIVE_DATE, PATIENT_VFC_STATUS, PATIENT_WIC_ID, VACCINATION_ACTION_CODE, VACCINATION_ADMIN_CODE, VACCINATION_ADMIN_CODE_TABLE, VACCINATION_ADMIN_DATE, VACCINATION_ADMIN_DATE_END, VACCINATION_ADMINISTERED_AMOUNT, VACCINATION_ADMINISTERED_UNIT, VACCINATION_BODY_ROUTE, VACCINATION_BODY_SITE, VACCINATION_COMPLETION_STATUS, VACCINATION_CONFIDENTIALITY_CODE, VACCINATION_CPT_CODE, VACCINATION_CVX_CODE, VACCINATION_CVX_CODE_AND_CPT_CODE, VACCINATION_FACILITY_ID, VACCINATION_FACILITY_NAME, VACCINATION_FACILITY_TYPE, VACCINATION_FILLER_ORDER_NUMBER, VACCINATION_FINANCIAL_ELIGIBILITY_CODE, VACCINATION_GIVEN_BY, VACCINATION_ID, VACCINATION_ID_OF_RECEIVER, VACCINATION_ID_OF_SENDER, VACCINATION_INFORMATION_SOURCE, VACCINATION_VIS, VACCINATION_VIS_CVX_CODE, VACCINATION_VIS_DOCUMENT_TYPE, VACCINATION_VIS_PUBLISHED_DATE, VACCINATION_VIS_PRESENTED_DATE, VACCINATION_LOT_EXPIRATION_DATE, VACCINATION_LOT_NUMBER, VACCINATION_MANUFACTURER_CODE, VACCINATION_ORDER_CONTROL_CODE, VACCINATION_ORDER_FACILITY_ID, VACCINATION_ORDER_FACILITY_NAME, VACCINATION_ORDERED_BY, VACCINATION_PLACER_ORDER_NUMBER, VACCINATION_PRODUCT, VACCINATION_RECORDED_BY, VACCINATION_REFUSAL_REASON, VACCINATION_SYSTEM_ENTRY_TIME, VACCINATION_TRADE_NAME, VACCINATION_TRADE_NAME_AND_VACCINE, VACCINATION_TRADE_NAME_AND_MANUFACTURER, VACCINATION_VALIDITY_CODE
  }

  private HashMap<Field, HashMap<String, PotentialIssue>> fieldIssueMaps = new HashMap<PotentialIssues.Field, HashMap<String, PotentialIssue>>();
  private List<PotentialIssue> allPotentialIssues = new ArrayList<PotentialIssue>();
  private Map<String, PotentialIssue> allPotentialIssuesMap = new HashMap<String, PotentialIssue>();

  private HashMap<Field, String> fieldDocumentation = new HashMap<PotentialIssues.Field, String>();

  private Properties documentationTextProperties = new Properties();;

  public List<PotentialIssue> getAllPotentialIssues()
  {
    return allPotentialIssues;
  }

  public List<Field> getAllFields()
  {
    return new ArrayList<Field>(fieldIssueMaps.keySet());
  }

  /**
   * Returns the potential issue that has the given display text. If the
   * potential issue is not found then null is returned.
   * 
   * @param displayText
   * @return
   */
  public PotentialIssue getPotentialIssueByDisplayText(String displayText)
  {
    return allPotentialIssuesMap.get(displayText);
  }

  public PotentialIssue getPotentialIssueByDisplayText(String displayText, String issueType)
  {
    return allPotentialIssuesMap.get(displayText + " " + issueType);
  }

  private void addToFieldIssueMap(Field field, PotentialIssue issue)
  {
    HashMap<String, PotentialIssue> issueMap = fieldIssueMaps.get(field);
    if (issueMap == null)
    {
      issueMap = new HashMap<String, PotentialIssue>();
      fieldIssueMaps.put(field, issueMap);

      fieldDocumentation.put(field, readDocumentation(field));
    }
    if (issue.getIssueType().equals(PotentialIssue.ISSUE_TYPE_IS_VALUED_AS))
    {
      // don't add valued_as to the map
      return;
    }
    issueMap.put(issue.getIssueType(), issue);
  }

  private String readDocumentation(Field field)
  {
    try
    {
      InputStream is = getClass().getResourceAsStream("/org/openimmunizationsoftware/dqa/validate/issues/" + field.toString() + ".html");
      if (is == null)
      {
        return "<h3>" + field.toString() + "</h3><p>#### No documentation available ####</p>";
      } else
      {
        return convertStreamToString(is);
      }
    } catch (Exception e)
    {
      return "<h3>" + field.toString() + "</h3><p>Unable to load documentation: " + e.toString() + "</p>";
    }
  }

  private String convertStreamToString(InputStream is) throws IOException
  {
    if (is != null)
    {
      Writer writer = new StringWriter();
      char[] buffer = new char[1024];
      try
      {
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        int n;
        while ((n = reader.read(buffer)) != -1)
        {
          writer.write(buffer, 0, n);
        }

      } finally
      {
        is.close();
      }
      return writer.toString();
    } else

    {
      return "";
    }
  }

  public PotentialIssue getIssue(Field field, String issueType)
  {
    PotentialIssue issue = null;
    HashMap<String, PotentialIssue> issueMap = fieldIssueMaps.get(field);
    if (issueMap != null)
    {
      issue = issueMap.get(issueType);
    }
    return issue;
  }

  private static PotentialIssues singleton = null;

  private static String INIT_BLOCK = "";

  public static PotentialIssues getPotentialIssues()
  {
    if (singleton == null)
    {
      synchronized (INIT_BLOCK)
      {
        if (singleton == null)
        {
          singleton = new PotentialIssues();
          ReloadManager.registerReload(singleton);
        }
      }
    }
    return singleton;
  }

  public void reload()
  {
    singleton = null;
    getPotentialIssues();
  }
  public String getLongDescription(PotentialIssue issue)
  {
	  String description = documentationTextProperties.getProperty(issue.getDisplayText());
      if (description == null)
      {

        if (issue.getFieldValue() != null && !issue.getFieldValue().equals(""))
        {
          description = documentationTextProperties.getProperty(issue.getIssueType() + " " + issue.getFieldValue());
        } else
        {
          description = documentationTextProperties.getProperty(issue.getIssueType());
        }
        if (description == null)
        {
          description = "#### NOT FOUND #### USE " + issue.getIssueType() + "=";
        }
      }
      return description;
      
  }
  private PotentialIssues() {
    SessionFactory factory = OrganizationManager.getSessionFactory();
    Session session = factory.openSession();
    Transaction tx = session.beginTransaction();

    GeneralAuthorizationException = getPotentialIssue(session, "General", "authorization", "exception", "");
    GeneralConfigurationException = getPotentialIssue(session, "General", "configuration", "exception", "");
    GeneralParseException = getPotentialIssue(session, "General", "parse", "exception", "");
    GeneralProcessingException = getPotentialIssue(session, "General", "processing", "exception", "");
    Hl7SegmentIsUnrecognized = getPotentialIssue(session, "HL7", "segment", "is unrecognized", "");
    Hl7SegmentIsInvalid = getPotentialIssue(session, "HL7", "segment", "is invalid", "");
    Hl7SegmentsOutOfOrder = getPotentialIssue(session, "HL7", "segments", "out of order", "");
    Hl7MshAcceptAckTypeIsDeprecated = getPotentialIssue(session, "HL7 MSH", "accept ack type", "is deprecated", "");
    Hl7MshAcceptAckTypeIsIgnored = getPotentialIssue(session, "HL7 MSH", "accept ack type", "is ignored", "");
    Hl7MshAcceptAckTypeIsInvalid = getPotentialIssue(session, "HL7 MSH", "accept ack type", "is invalid", "");
    Hl7MshAcceptAckTypeIsMissing = getPotentialIssue(session, "HL7 MSH", "accept ack type", "is missing", "");
    Hl7MshAcceptAckTypeIsUnrecognized = getPotentialIssue(session, "HL7 MSH", "accept ack type", "is unrecognized", "");
    Hl7MshAcceptAckTypeIsValuedAsAlways = getPotentialIssue(session, "HL7 MSH", "accept ack type", "is valued as", "always");
    Hl7MshAcceptAckTypeIsValuedAsNever = getPotentialIssue(session, "HL7 MSH", "accept ack type", "is valued as", "never");
    Hl7MshAcceptAckTypeIsValuedAsOnlyOnErrors = getPotentialIssue(session, "HL7 MSH", "accept ack type", "is valued as", "only on errors");
    Hl7MshAltCharacterSetIsDeprecated = getPotentialIssue(session, "HL7 MSH", "alt character set", "is deprecated", "");
    Hl7MshAltCharacterSetIsIgnored = getPotentialIssue(session, "HL7 MSH", "alt character set", "is ignored", "");
    Hl7MshAltCharacterSetIsInvalid = getPotentialIssue(session, "HL7 MSH", "alt character set", "is invalid", "");
    Hl7MshAltCharacterSetIsMissing = getPotentialIssue(session, "HL7 MSH", "alt character set", "is missing", "");
    Hl7MshAltCharacterSetIsUnrecognized = getPotentialIssue(session, "HL7 MSH", "alt character set", "is unrecognized", "");
    Hl7MshAppAckTypeIsDeprecated = getPotentialIssue(session, "HL7 MSH", "app ack type", "is deprecated", "");
    Hl7MshAppAckTypeIsIgnored = getPotentialIssue(session, "HL7 MSH", "app ack type", "is ignored", "");
    Hl7MshAppAckTypeIsInvalid = getPotentialIssue(session, "HL7 MSH", "app ack type", "is invalid", "");
    Hl7MshAppAckTypeIsMissing = getPotentialIssue(session, "HL7 MSH", "app ack type", "is missing", "");
    Hl7MshAppAckTypeIsUnrecognized = getPotentialIssue(session, "HL7 MSH", "app ack type", "is unrecognized", "");
    Hl7MshAppAckTypeIsValuedAsAlways = getPotentialIssue(session, "HL7 MSH", "app ack type", "is valued as", "always");
    Hl7MshAppAckTypeIsValuedAsNever = getPotentialIssue(session, "HL7 MSH", "app ack type", "is valued as", "never");
    Hl7MshAppAckTypeIsValuedAsOnlyOnErrors = getPotentialIssue(session, "HL7 MSH", "app ack type", "is valued as", "only on errors");
    Hl7MshCharacterSetIsDeprecated = getPotentialIssue(session, "HL7 MSH", "character set", "is deprecated", "");
    Hl7MshCharacterSetIsIgnored = getPotentialIssue(session, "HL7 MSH", "character set", "is ignored", "");
    Hl7MshCharacterSetIsInvalid = getPotentialIssue(session, "HL7 MSH", "character set", "is invalid", "");
    Hl7MshCharacterSetIsMissing = getPotentialIssue(session, "HL7 MSH", "character set", "is missing", "");
    Hl7MshCharacterSetIsUnrecognized = getPotentialIssue(session, "HL7 MSH", "character set", "is unrecognized", "");
    Hl7MshCountryCodeIsDeprecated = getPotentialIssue(session, "HL7 MSH", "country code", "is deprecated", "");
    Hl7MshCountryCodeIsIgnored = getPotentialIssue(session, "HL7 MSH", "country code", "is ignored", "");
    Hl7MshCountryCodeIsInvalid = getPotentialIssue(session, "HL7 MSH", "country code", "is invalid", "");
    Hl7MshCountryCodeIsMissing = getPotentialIssue(session, "HL7 MSH", "country code", "is missing", "");
    Hl7MshCountryCodeIsUnrecognized = getPotentialIssue(session, "HL7 MSH", "country code", "is unrecognized", "");
    Hl7MshEncodingCharacterIsInvalid = getPotentialIssue(session, "HL7 MSH", "encoding character", "is invalid", "");
    Hl7MshEncodingCharacterIsMissing = getPotentialIssue(session, "HL7 MSH", "encoding character", "is missing", "");
    Hl7MshEncodingCharacterIsNonStandard = getPotentialIssue(session, "HL7 MSH", "encoding character", "is non-standard", "");
    Hl7MshMessageControlIdIsMissing = getPotentialIssue(session, "HL7 MSH", "message control id", "is missing", "");
    Hl7MshMessageDateIsInFuture = getPotentialIssue(session, "HL7 MSH", "message date", "is in future", "");
    Hl7MshMessageDateIsInvalid = getPotentialIssue(session, "HL7 MSH", "message date", "is invalid", "");
    Hl7MshMessageDateIsMissing = getPotentialIssue(session, "HL7 MSH", "message date", "is missing", "");
    Hl7MshMessageDateIsNotPrecise = getPotentialIssue(session, "HL7 MSH", "message date", "is not precise", "");
    Hl7MshMessageDateIsMissingTimezone = getPotentialIssue(session, "HL7 MSH", "message date", "is missing timezone", "");
    Hl7MshMessageProfileIdIsDeprecated = getPotentialIssue(session, "HL7 MSH", "message profile id", "is deprecated", "");
    Hl7MshMessageProfileIdIsIgnored = getPotentialIssue(session, "HL7 MSH", "message profile id", "is ignored", "");
    Hl7MshMessageProfileIdIsInvalid = getPotentialIssue(session, "HL7 MSH", "message profile id", "is invalid", "");
    Hl7MshMessageProfileIdIsMissing = getPotentialIssue(session, "HL7 MSH", "message profile id", "is missing", "");
    Hl7MshMessageProfileIdIsUnrecognized = getPotentialIssue(session, "HL7 MSH", "message profile id", "is unrecognized", "");
    Hl7MshMessageStructureIsMissing = getPotentialIssue(session, "HL7 MSH", "message structure", "is missing", "");
    Hl7MshMessageStructureIsUnrecognized = getPotentialIssue(session, "HL7 MSH", "message structure", "is unrecognized", "");
    Hl7MshMessageTriggerIsMissing = getPotentialIssue(session, "HL7 MSH", "message trigger", "is missing", "");
    Hl7MshMessageTriggerIsUnrecognized = getPotentialIssue(session, "HL7 MSH", "message trigger", "is unrecognized", "");
    Hl7MshMessageTriggerIsUnsupported = getPotentialIssue(session, "HL7 MSH", "message trigger", "is unsupported", "");
    Hl7MshMessageTypeIsMissing = getPotentialIssue(session, "HL7 MSH", "message type", "is missing", "");
    Hl7MshMessageTypeIsUnrecognized = getPotentialIssue(session, "HL7 MSH", "message type", "is unrecognized", "");
    Hl7MshMessageTypeIsUnsupported = getPotentialIssue(session, "HL7 MSH", "message type", "is unsupported", "");
    Hl7MshProcessingIdIsDeprecated = getPotentialIssue(session, "HL7 MSH", "processing id", "is deprecated", "");
    Hl7MshProcessingIdIsIgnored = getPotentialIssue(session, "HL7 MSH", "processing id", "is ignored", "");
    Hl7MshProcessingIdIsInvalid = getPotentialIssue(session, "HL7 MSH", "processing id", "is invalid", "");
    Hl7MshProcessingIdIsMissing = getPotentialIssue(session, "HL7 MSH", "processing id", "is missing", "");
    Hl7MshProcessingIdIsUnrecognized = getPotentialIssue(session, "HL7 MSH", "processing id", "is unrecognized", "");
    Hl7MshProcessingIdIsUnsupported = getPotentialIssue(session, "HL7 MSH", "processing id", "is unsupported", "");
    Hl7MshProcessingIdIsValuedAsDebug = getPotentialIssue(session, "HL7 MSH", "processing id", "is valued as", "debug");
    Hl7MshProcessingIdIsValuedAsProduction = getPotentialIssue(session, "HL7 MSH", "processing id", "is valued as", "production");
    Hl7MshProcessingIdIsValuedAsTraining = getPotentialIssue(session, "HL7 MSH", "processing id", "is valued as", "training");
    Hl7MshReceivingApplicationIsInvalid = getPotentialIssue(session, "HL7 MSH", "receiving application", "is invalid", "");
    Hl7MshReceivingApplicationIsMissing = getPotentialIssue(session, "HL7 MSH", "receiving application", "is missing", "");
    Hl7MshReceivingFacilityIsInvalid = getPotentialIssue(session, "HL7 MSH", "receiving facility", "is invalid", "");
    Hl7MshReceivingFacilityIsMissing = getPotentialIssue(session, "HL7 MSH", "receiving facility", "is missing", "");
    Hl7MshSegmentIsMissing = getPotentialIssue(session, "HL7 MSH", "segment", "is missing", "");
    Hl7MshSendingApplicationIsInvalid = getPotentialIssue(session, "HL7 MSH", "sending application", "is invalid", "");
    Hl7MshSendingApplicationIsMissing = getPotentialIssue(session, "HL7 MSH", "sending application", "is missing", "");
    Hl7MshSendingFacilityIsInvalid = getPotentialIssue(session, "HL7 MSH", "sending facility", "is invalid", "");
    Hl7MshSendingFacilityIsMissing = getPotentialIssue(session, "HL7 MSH", "sending facility", "is missing", "");
    Hl7MshVersionIsMissing = getPotentialIssue(session, "HL7 MSH", "version", "is missing", "");
    Hl7MshVersionIsUnrecognized = getPotentialIssue(session, "HL7 MSH", "version", "is unrecognized", "");
    Hl7MshVersionIsInvalid = getPotentialIssue(session, "HL7 MSH", "version", "is invalid", "");
    Hl7MshVersionIsValuedAs2_3_1 = getPotentialIssue(session, "HL7 MSH", "version", "is valued as", "2.3.1");
    Hl7MshVersionIsValuedAs2_4 = getPotentialIssue(session, "HL7 MSH", "version", "is valued as", "2.4");
    Hl7MshVersionIsValuedAs2_5 = getPotentialIssue(session, "HL7 MSH", "version", "is valued as", "2.5");
    Hl7Nk1SegmentIsMissing = getPotentialIssue(session, "HL7 NK1", "segment", "is missing", "");
    Hl7Nk1SegmentIsRepeated = getPotentialIssue(session, "HL7 NK1", "segment", "is repeated", "");
    Hl7Nk1SetIdIsMissing = getPotentialIssue(session, "HL7 NK1", "set id", "is missing", "");
    Hl7ObxSegmentIsMissing = getPotentialIssue(session, "HL7 OBX", "segment", "is missing", "");
    Hl7OrcSegmentIsMissing = getPotentialIssue(session, "HL7 ORC", "segment", "is missing", "");
    Hl7OrcSegmentIsRepeated = getPotentialIssue(session, "HL7 ORC", "segment", "is repeated", "");
    Hl7Pd1SegmentIsMissing = getPotentialIssue(session, "HL7 PD1", "segment", "is missing", "");
    Hl7PidSegmentIsMissing = getPotentialIssue(session, "HL7 PID", "segment", "is missing", "");
    Hl7PidSegmentIsRepeated = getPotentialIssue(session, "HL7 PID", "segment", "is repeated", "");
    Hl7Pv1SegmentIsMissing = getPotentialIssue(session, "HL7 PV1", "segment", "is missing", "");
    Hl7Pv1SegmentIsRepeated = getPotentialIssue(session, "HL7 PV1", "segment", "is repeated", "");
    Hl7RxaAdminSubIdCounterIsMissing = getPotentialIssue(session, "HL7 RXA", "admin sub id counter", "is missing", "");
    Hl7RxaGiveSubIdIsMissing = getPotentialIssue(session, "HL7 RXA", "give sub id", "is missing", "");
    Hl7RxaSegmentIsMissing = getPotentialIssue(session, "HL7 RXA", "segment", "is missing", "");
    Hl7RxaSegmentIsRepeated = getPotentialIssue(session, "HL7 RXA", "segment", "is repeated", "");
    Hl7RxrSegmentIsMissing = getPotentialIssue(session, "HL7 RXR", "segment", "is missing", "");
    Hl7RxrSegmentIsRepeated = getPotentialIssue(session, "HL7 RXR", "segment", "is repeated", "");
    NextOfKinAddressIsDifferentFromPatientAddress = getPotentialIssue(session, "Next-of-kin", "address", "is different from patient address", "");
    NextOfKinAddressIsMissing = getPotentialIssue(session, "Next-of-kin", "address", "is missing", "");
    NextOfKinAddressCityIsInvalid = getPotentialIssue(session, "Next-of-kin", "address city", "is invalid", "");
    NextOfKinAddressCityIsMissing = getPotentialIssue(session, "Next-of-kin", "address city", "is missing", "");
    NextOfKinAddressCityIsTooShort = getPotentialIssue(session, "Next-of-kin", "address city", "is too short", "");
    NextOfKinAddressCityIsUnexpectedlyShort = getPotentialIssue(session, "Next-of-kin", "address city", "is unexpectedly short", "");
    NextOfKinAddressCityIsUnexpectedlyLong = getPotentialIssue(session, "Next-of-kin", "address city", "is unexpectedly long", "");
    NextOfKinAddressCityIsTooLong = getPotentialIssue(session, "Next-of-kin", "address city", "is too long", "");
    NextOfKinAddressCountryIsDeprecated = getPotentialIssue(session, "Next-of-kin", "address country", "is deprecated", "");
    NextOfKinAddressCountryIsIgnored = getPotentialIssue(session, "Next-of-kin", "address country", "is ignored", "");
    NextOfKinAddressCountryIsInvalid = getPotentialIssue(session, "Next-of-kin", "address country", "is invalid", "");
    NextOfKinAddressCountryIsMissing = getPotentialIssue(session, "Next-of-kin", "address country", "is missing", "");
    NextOfKinAddressCountryIsUnrecognized = getPotentialIssue(session, "Next-of-kin", "address country", "is unrecognized", "");
    NextOfKinAddressCountyIsDeprecated = getPotentialIssue(session, "Next-of-kin", "address county", "is deprecated", "");
    NextOfKinAddressCountyIsIgnored = getPotentialIssue(session, "Next-of-kin", "address county", "is ignored", "");
    NextOfKinAddressCountyIsInvalid = getPotentialIssue(session, "Next-of-kin", "address county", "is invalid", "");
    NextOfKinAddressCountyIsMissing = getPotentialIssue(session, "Next-of-kin", "address county", "is missing", "");
    NextOfKinAddressCountyIsUnrecognized = getPotentialIssue(session, "Next-of-kin", "address county", "is unrecognized", "");
    NextOfKinAddressStateIsDeprecated = getPotentialIssue(session, "Next-of-kin", "address state", "is deprecated", "");
    NextOfKinAddressStateIsIgnored = getPotentialIssue(session, "Next-of-kin", "address state", "is ignored", "");
    NextOfKinAddressStateIsInvalid = getPotentialIssue(session, "Next-of-kin", "address state", "is invalid", "");
    NextOfKinAddressStateIsMissing = getPotentialIssue(session, "Next-of-kin", "address state", "is missing", "");
    NextOfKinAddressStateIsUnrecognized = getPotentialIssue(session, "Next-of-kin", "address state", "is unrecognized", "");
    NextOfKinAddressStreetIsMissing = getPotentialIssue(session, "Next-of-kin", "address street", "is missing", "");
    NextOfKinAddressStreet2IsMissing = getPotentialIssue(session, "Next-of-kin", "address street2", "is missing", "");
    NextOfKinAddressTypeIsDeprecated = getPotentialIssue(session, "Next-of-kin", "address type", "is deprecated", "");
    NextOfKinAddressTypeIsIgnored = getPotentialIssue(session, "Next-of-kin", "address type", "is ignored", "");
    NextOfKinAddressTypeIsInvalid = getPotentialIssue(session, "Next-of-kin", "address type", "is invalid", "");
    NextOfKinAddressTypeIsMissing = getPotentialIssue(session, "Next-of-kin", "address type", "is missing", "");
    NextOfKinAddressTypeIsUnrecognized = getPotentialIssue(session, "Next-of-kin", "address type", "is unrecognized", "");
    NextOfKinAddressTypeIsValuedBadAddress = getPotentialIssue(session, "Next-of-kin", "address type", "is valued bad address", "");
    NextOfKinAddressZipIsInvalid = getPotentialIssue(session, "Next-of-kin", "address zip", "is invalid", "");
    NextOfKinAddressZipIsMissing = getPotentialIssue(session, "Next-of-kin", "address zip", "is missing", "");
    NextOfKinNameIsMissing = getPotentialIssue(session, "Next-of-kin", "name", "is missing", "");
    NextOfKinNameFirstIsMissing = getPotentialIssue(session, "Next-of-kin", "name first", "is missing", "");
    NextOfKinNameFirstIsTooShort = getPotentialIssue(session, "Next-of-kin", "name first", "is too short", "");
    NextOfKinNameFirstIsUnexpectedlyShort = getPotentialIssue(session, "Next-of-kin", "name first", "is unexpectedly short", "");
    NextOfKinNameFirstIsUnexpectedlyLong = getPotentialIssue(session, "Next-of-kin", "name first", "is unexpectedly long", "");
    NextOfKinNameFirstIsTooLong = getPotentialIssue(session, "Next-of-kin", "name first", "is too long", "");
    NextOfKinNameLastIsMissing = getPotentialIssue(session, "Next-of-kin", "name last", "is missing", "");
    NextOfKinNameLastIsTooShort = getPotentialIssue(session, "Next-of-kin", "name last", "is too short", "");
    NextOfKinNameLastIsUnexpectedlyShort = getPotentialIssue(session, "Next-of-kin", "name last", "is unexpectedly short", "");
    NextOfKinNameLastIsUnexpectedlyLong = getPotentialIssue(session, "Next-of-kin", "name last", "is unexpectedly long", "");
    NextOfKinNameLastIsTooLong = getPotentialIssue(session, "Next-of-kin", "name last", "is too long", "");
    NextOfKinPhoneNumberIsIncomplete = getPotentialIssue(session, "Next-of-kin", "phone number", "is incomplete", "");
    NextOfKinPhoneNumberIsInvalid = getPotentialIssue(session, "Next-of-kin", "phone number", "is invalid", "");
    NextOfKinPhoneNumberIsMissing = getPotentialIssue(session, "Next-of-kin", "phone number", "is missing", "");
    NextOfKinRelationshipIsDeprecated = getPotentialIssue(session, "Next-of-kin", "relationship", "is deprecated", "");
    NextOfKinRelationshipIsIgnored = getPotentialIssue(session, "Next-of-kin", "relationship", "is ignored", "");
    NextOfKinRelationshipIsInvalid = getPotentialIssue(session, "Next-of-kin", "relationship", "is invalid", "");
    NextOfKinRelationshipIsMissing = getPotentialIssue(session, "Next-of-kin", "relationship", "is missing", "");
    NextOfKinRelationshipIsNotResponsibleParty = getPotentialIssue(session, "Next-of-kin", "relationship", "is not responsible party", "");
    NextOfKinRelationshipIsUnexpected = getPotentialIssue(session, "Next-of-kin", "relationship", "is unexpected", "");
    NextOfKinRelationshipIsUnrecognized = getPotentialIssue(session, "Next-of-kin", "relationship", "is unrecognized", "");
    NextOfKinSsnIsMissing = getPotentialIssue(session, "Next-of-kin", "SSN", "is missing", "");
    ObservationValueTypeIsDeprecated = getPotentialIssue(session, "Observation", "value type", "is deprecated", "");
    ObservationValueTypeIsIgnored = getPotentialIssue(session, "Observation", "value type", "is ignored", "");
    ObservationValueTypeIsInvalid = getPotentialIssue(session, "Observation", "value type", "is invalid", "");
    ObservationValueTypeIsMissing = getPotentialIssue(session, "Observation", "value type", "is missing", "");
    ObservationValueTypeIsUnrecognized = getPotentialIssue(session, "Observation", "value type", "is unrecognized", "");
    ObservationIdentifierCodeIsDeprecated = getPotentialIssue(session, "Observation", "identifier code", "is deprecated", "");
    ObservationIdentifierCodeIsIgnored = getPotentialIssue(session, "Observation", "identifier code", "is ignored", "");
    ObservationIdentifierCodeIsInvalid = getPotentialIssue(session, "Observation", "identifier code", "is invalid", "");
    ObservationIdentifierCodeIsMissing = getPotentialIssue(session, "Observation", "identifier code", "is missing", "");
    ObservationIdentifierCodeIsUnrecognized = getPotentialIssue(session, "Observation", "identifier code", "is unrecognized", "");
    ObservationValueIsMissing = getPotentialIssue(session, "Observation", "value", "is missing", "");
    ObservationDateTimeOfObservationIsMissing = getPotentialIssue(session, "Observation", "date time of observation", "is missing", "");
    ObservationDateTimeOfObservationIsInvalid = getPotentialIssue(session, "Observation", "date time of observation", "is invalid", "");
    PatientAddressIsMissing = getPotentialIssue(session, "Patient", "address", "is missing", "");
    PatientAddressCityIsInvalid = getPotentialIssue(session, "Patient", "address city", "is invalid", "");
    PatientAddressCityIsMissing = getPotentialIssue(session, "Patient", "address city", "is missing", "");
    PatientAddressCityIsTooShort = getPotentialIssue(session, "Patient", "address city", "is too short", "");
    PatientAddressCityIsUnexpectedlyShort = getPotentialIssue(session, "Patient", "address city", "is unexpectedly short", "");
    PatientAddressCityIsUnexpectedlyLong = getPotentialIssue(session, "Patient", "address city", "is unexpectedly long", "");
    PatientAddressCityIsTooLong = getPotentialIssue(session, "Patient", "address city", "is too long", "");
    PatientAddressCountryIsDeprecated = getPotentialIssue(session, "Patient", "address country", "is deprecated", "");
    PatientAddressCountryIsIgnored = getPotentialIssue(session, "Patient", "address country", "is ignored", "");
    PatientAddressCountryIsInvalid = getPotentialIssue(session, "Patient", "address country", "is invalid", "");
    PatientAddressCountryIsMissing = getPotentialIssue(session, "Patient", "address country", "is missing", "");
    PatientAddressCountryIsUnrecognized = getPotentialIssue(session, "Patient", "address country", "is unrecognized", "");
    PatientAddressCountyIsDeprecated = getPotentialIssue(session, "Patient", "address county", "is deprecated", "");
    PatientAddressCountyIsIgnored = getPotentialIssue(session, "Patient", "address county", "is ignored", "");
    PatientAddressCountyIsInvalid = getPotentialIssue(session, "Patient", "address county", "is invalid", "");
    PatientAddressCountyIsMissing = getPotentialIssue(session, "Patient", "address county", "is missing", "");
    PatientAddressCountyIsUnrecognized = getPotentialIssue(session, "Patient", "address county", "is unrecognized", "");
    PatientAddressStateIsDeprecated = getPotentialIssue(session, "Patient", "address state", "is deprecated", "");
    PatientAddressStateIsIgnored = getPotentialIssue(session, "Patient", "address state", "is ignored", "");
    PatientAddressStateIsInvalid = getPotentialIssue(session, "Patient", "address state", "is invalid", "");
    PatientAddressStateIsMissing = getPotentialIssue(session, "Patient", "address state", "is missing", "");
    PatientAddressStateIsUnrecognized = getPotentialIssue(session, "Patient", "address state", "is unrecognized", "");
    PatientAddressStreetIsMissing = getPotentialIssue(session, "Patient", "address street", "is missing", "");
    PatientAddressStreet2IsMissing = getPotentialIssue(session, "Patient", "address street2", "is missing", "");
    PatientAddressTypeIsMissing = getPotentialIssue(session, "Patient", "address type", "is missing", "");
    PatientAddressTypeIsDeprecated = getPotentialIssue(session, "Patient", "address type", "is deprecated", "");
    PatientAddressTypeIsIgnored = getPotentialIssue(session, "Patient", "address type", "is ignored", "");
    PatientAddressTypeIsInvalid = getPotentialIssue(session, "Patient", "address type", "is invalid", "");
    PatientAddressTypeIsUnrecognized = getPotentialIssue(session, "Patient", "address type", "is unrecognized", "");
    PatientAddressTypeIsValuedBadAddress = getPotentialIssue(session, "Patient", "address type", "is valued bad address", "");
    PatientAddressZipIsInvalid = getPotentialIssue(session, "Patient", "address zip", "is invalid", "");
    PatientAddressZipIsMissing = getPotentialIssue(session, "Patient", "address zip", "is missing", "");
    PatientAliasIsMissing = getPotentialIssue(session, "Patient", "alias", "is missing", "");
    PatientBirthDateIsAfterSubmission = getPotentialIssue(session, "Patient", "birth date", "is after submission", "");
    PatientBirthDateIsInFuture = getPotentialIssue(session, "Patient", "birth date", "is in future", "");
    PatientBirthDateIsInvalid = getPotentialIssue(session, "Patient", "birth date", "is invalid", "");
    PatientBirthDateIsMissing = getPotentialIssue(session, "Patient", "birth date", "is missing", "");
    PatientBirthDateIsUnderage = getPotentialIssue(session, "Patient", "birth date", "is underage", "");
    PatientBirthDateIsVeryLongAgo = getPotentialIssue(session, "Patient", "birth date", "is very long ago", "");
    PatientBirthIndicatorIsInvalid = getPotentialIssue(session, "Patient", "birth indicator", "is invalid", "");
    PatientBirthIndicatorIsMissing = getPotentialIssue(session, "Patient", "birth indicator", "is missing", "");
    PatientBirthOrderIsInvalid = getPotentialIssue(session, "Patient", "birth order", "is invalid", "");
    PatientBirthOrderIsMissing = getPotentialIssue(session, "Patient", "birth order", "is missing", "");
    PatientBirthOrderIsMissingAndMultipleBirthIndicated = getPotentialIssue(session, "Patient", "birth order", "is missing and multiple birth indicated", "");
    PatientBirthPlaceIsMissing = getPotentialIssue(session, "Patient", "birth place", "is missing", "");
    PatientBirthPlaceIsTooShort = getPotentialIssue(session, "Patient", "birth place", "is too short", "");
    PatientBirthPlaceIsUnexpectedlyShort = getPotentialIssue(session, "Patient", "birth place", "is unexpectedly short", "");
    PatientBirthPlaceIsUnexpectedlyLong = getPotentialIssue(session, "Patient", "birth place", "is unexpectedly long", "");
    PatientBirthPlaceIsTooLong = getPotentialIssue(session, "Patient", "birth place", "is too long", "");
    PatientBirthRegistryIdIsInvalid = getPotentialIssue(session, "Patient", "birth registry id", "is invalid", "");
    PatientBirthRegistryIdIsMissing = getPotentialIssue(session, "Patient", "birth registry id", "is missing", "");
    PatientClassIsDeprecated = getPotentialIssue(session, "Patient", "class", "is deprecated", "");
    PatientClassIsIgnored = getPotentialIssue(session, "Patient", "class", "is ignored", "");
    PatientClassIsInvalid = getPotentialIssue(session, "Patient", "class", "is invalid", "");
    PatientClassIsMissing = getPotentialIssue(session, "Patient", "class", "is missing", "");
    PatientClassIsUnrecognized = getPotentialIssue(session, "Patient", "class", "is unrecognized", "");
    PatientDeathDateIsBeforeBirth = getPotentialIssue(session, "Patient", "death date", "is before birth", "");
    PatientDeathDateIsInFuture = getPotentialIssue(session, "Patient", "death date", "is in future", "");
    PatientDeathDateIsInvalid = getPotentialIssue(session, "Patient", "death date", "is invalid", "");
    PatientDeathDateIsMissing = getPotentialIssue(session, "Patient", "death date", "is missing", "");
    PatientDeathIndicatorIsInconsistent = getPotentialIssue(session, "Patient", "death indicator", "is inconsistent", "");
    PatientDeathIndicatorIsMissing = getPotentialIssue(session, "Patient", "death indicator", "is missing", "");
    PatientEthnicityIsDeprecated = getPotentialIssue(session, "Patient", "ethnicity", "is deprecated", "");
    PatientEthnicityIsIgnored = getPotentialIssue(session, "Patient", "ethnicity", "is ignored", "");
    PatientEthnicityIsInvalid = getPotentialIssue(session, "Patient", "ethnicity", "is invalid", "");
    PatientEthnicityIsMissing = getPotentialIssue(session, "Patient", "ethnicity", "is missing", "");
    PatientEthnicityIsUnrecognized = getPotentialIssue(session, "Patient", "ethnicity", "is unrecognized", "");
    PatientGenderIsDeprecated = getPotentialIssue(session, "Patient", "gender", "is deprecated", "");
    PatientGenderIsIgnored = getPotentialIssue(session, "Patient", "gender", "is ignored", "");
    PatientGenderIsInvalid = getPotentialIssue(session, "Patient", "gender", "is invalid", "");
    PatientGenderIsMissing = getPotentialIssue(session, "Patient", "gender", "is missing", "");
    PatientGenderIsUnrecognized = getPotentialIssue(session, "Patient", "gender", "is unrecognized", "");
    PatientGuardianAddressIsMissing = getPotentialIssue(session, "Patient", "guardian address", "is missing", "");
    PatientGuardianAddressCityIsMissing = getPotentialIssue(session, "Patient", "guardian address city", "is missing", "");
    PatientGuardianAddressStateIsMissing = getPotentialIssue(session, "Patient", "guardian address state", "is missing", "");
    PatientGuardianAddressStreetIsMissing = getPotentialIssue(session, "Patient", "guardian address street", "is missing", "");
    PatientGuardianAddressZipIsMissing = getPotentialIssue(session, "Patient", "guardian address zip", "is missing", "");
    PatientGuardianNameIsMissing = getPotentialIssue(session, "Patient", "guardian name", "is missing", "");
    PatientGuardianNameIsSameAsUnderagePatient = getPotentialIssue(session, "Patient", "guardian name", "is same as underage patient", "");
    PatientGuardianNameHasJunkName = getPotentialIssue(session, "Patient", "guardian name", "has junk name", "");
    PatientGuardianNameFirstIsMissing = getPotentialIssue(session, "Patient", "guardian name first", "is missing", "");
    PatientGuardianNameLastIsMissing = getPotentialIssue(session, "Patient", "guardian name last", "is missing", "");
    PatientGuardianResponsiblePartyIsMissing = getPotentialIssue(session, "Patient", "guardian responsible party", "is missing", "");
    PatientGuardianPhoneIsMissing = getPotentialIssue(session, "Patient", "guardian phone", "is missing", "");
    PatientGuardianRelationshipIsMissing = getPotentialIssue(session, "Patient", "guardian relationship", "is missing", "");
    PatientImmunityCodeIsDeprecated = getPotentialIssue(session, "Patient", "immunity code", "is deprecated", "");
    PatientImmunityCodeIsIgnored = getPotentialIssue(session, "Patient", "immunity code", "is ignored", "");
    PatientImmunityCodeIsInvalid = getPotentialIssue(session, "Patient", "immunity code", "is invalid", "");
    PatientImmunityCodeIsMissing = getPotentialIssue(session, "Patient", "immunity code", "is missing", "");
    PatientImmunityCodeIsUnrecognized = getPotentialIssue(session, "Patient", "immunity code", "is unrecognized", "");
    PatientImmunizationRegistryStatusIsDeprecated = getPotentialIssue(session, "Patient", "immunization registry status", "is deprecated", "");
    PatientImmunizationRegistryStatusIsIgnored = getPotentialIssue(session, "Patient", "immunization registry status", "is ignored", "");
    PatientImmunizationRegistryStatusIsInvalid = getPotentialIssue(session, "Patient", "immunization registry status", "is invalid", "");
    PatientImmunizationRegistryStatusIsMissing = getPotentialIssue(session, "Patient", "immunization registry status", "is missing", "");
    PatientImmunizationRegistryStatusIsUnrecognized = getPotentialIssue(session, "Patient", "immunization registry status", "is unrecognized", "");
    PatientMedicaidNumberIsInvalid = getPotentialIssue(session, "Patient", "Medicaid number", "is invalid", "");
    PatientMedicaidNumberIsMissing = getPotentialIssue(session, "Patient", "Medicaid number", "is missing", "");
    PatientMiddleNameIsMissing = getPotentialIssue(session, "Patient", "middle name", "is missing", "");
    PatientMiddleNameIsInvalid = getPotentialIssue(session, "Patient", "middle name", "is invalid", "");
    PatientMiddleNameMayBeInitial = getPotentialIssue(session, "Patient", "middle name", "may be initial", "");
    PatientMiddleNameIsTooShort = getPotentialIssue(session, "Patient", "middle name", "is too short", "");
    PatientMiddleNameIsUnexpectedlyShort = getPotentialIssue(session, "Patient", "middle name", "is unexpectedly short", "");
    PatientMiddleNameIsUnexpectedlyLong = getPotentialIssue(session, "Patient", "middle name", "is unexpectedly long", "");
    PatientMiddleNameIsTooLong = getPotentialIssue(session, "Patient", "middle name", "is too long", "");
    PatientMotherSMaidenNameIsInvalid = getPotentialIssue(session, "Patient", "mother's maiden name", "is invalid", "");
    PatientMotherSMaidenNameHasJunkName = getPotentialIssue(session, "Patient", "mother's maiden name", "has junk name", "");
    PatientMotherSMaidenNameHasInvalidPrefixes = getPotentialIssue(session, "Patient", "mother's maiden name", "has invalid prefixes", "");
    PatientMotherSMaidenNameIsMissing = getPotentialIssue(session, "Patient", "mother's maiden name", "is missing", "");
    PatientMotherSMaidenNameIsTooShort = getPotentialIssue(session, "Patient", "mother's maiden name", "is too short", "");
    PatientMotherSMaidenNameIsUnexpectedlyShort = getPotentialIssue(session, "Patient", "mother's maiden name", "is unexpectedly short", "");
    PatientMotherSMaidenNameIsUnexpectedlyLong = getPotentialIssue(session, "Patient", "mother's maiden name", "is unexpectedly long", "");
    PatientMotherSMaidenNameIsTooLong = getPotentialIssue(session, "Patient", "mother's maiden name", "is too long", "");
    PatientNameMayBeTemporaryNewbornName = getPotentialIssue(session, "Patient", "name", "may be temporary newborn name", "");
    PatientNameMayBeTestName = getPotentialIssue(session, "Patient", "name", "may be test name", "");
    PatientNameHasJunkName = getPotentialIssue(session, "Patient", "name", "has junk name", "");
    PatientNameIsAKnownTestName = getPotentialIssue(session, "Patient", "name", "is a known test name", "");
    PatientNameFirstIsInvalid = getPotentialIssue(session, "Patient", "name first", "is invalid", "");
    PatientNameFirstIsMissing = getPotentialIssue(session, "Patient", "name first", "is missing", "");
    PatientNameFirstIsTooShort = getPotentialIssue(session, "Patient", "name first", "is too short", "");
    PatientNameFirstIsUnexpectedlyShort = getPotentialIssue(session, "Patient", "name first", "is unexpectedly short", "");
    PatientNameFirstIsUnexpectedlyLong = getPotentialIssue(session, "Patient", "name first", "is unexpectedly long", "");
    PatientNameFirstIsTooLong = getPotentialIssue(session, "Patient", "name first", "is too long", "");
    PatientNameFirstMayIncludeMiddleInitial = getPotentialIssue(session, "Patient", "name first", "may include middle initial", "");
    PatientNameLastIsInvalid = getPotentialIssue(session, "Patient", "name last", "is invalid", "");
    PatientNameLastIsMissing = getPotentialIssue(session, "Patient", "name last", "is missing", "");
    PatientNameLastIsTooShort = getPotentialIssue(session, "Patient", "name last", "is too short", "");
    PatientNameLastIsUnexpectedlyShort = getPotentialIssue(session, "Patient", "name last", "is unexpectedly short", "");
    PatientNameLastIsUnexpectedlyLong = getPotentialIssue(session, "Patient", "name last", "is unexpectedly long", "");
    PatientNameLastIsTooLong = getPotentialIssue(session, "Patient", "name last", "is too long", "");
    PatientNameTypeCodeIsDeprecated = getPotentialIssue(session, "Patient", "name type code", "is deprecated", "");
    PatientNameTypeCodeIsIgnored = getPotentialIssue(session, "Patient", "name type code", "is ignored", "");
    PatientNameTypeCodeIsInvalid = getPotentialIssue(session, "Patient", "name type code", "is invalid", "");
    PatientNameTypeCodeIsMissing = getPotentialIssue(session, "Patient", "name type code", "is missing", "");
    PatientNameTypeCodeIsUnrecognized = getPotentialIssue(session, "Patient", "name type code", "is unrecognized", "");
    PatientNameTypeCodeIsNotValuedLegal = getPotentialIssue(session, "Patient", "name type code", "is not valued legal", "");
    PatientPhoneIsIncomplete = getPotentialIssue(session, "Patient", "phone", "is incomplete", "");
    PatientPhoneIsInvalid = getPotentialIssue(session, "Patient", "phone", "is invalid", "");
    PatientPhoneIsMissing = getPotentialIssue(session, "Patient", "phone", "is missing", "");
    PatientPhoneTelUseCodeIsDeprecated = getPotentialIssue(session, "Patient", "phone tel use code", "is deprecated", "");
    PatientPhoneTelUseCodeIsIgnored = getPotentialIssue(session, "Patient", "phone tel use code", "is ignored", "");
    PatientPhoneTelUseCodeIsInvalid = getPotentialIssue(session, "Patient", "phone tel use code", "is invalid", "");
    PatientPhoneTelUseCodeIsMissing = getPotentialIssue(session, "Patient", "phone tel use code", "is missing", "");
    PatientPhoneTelUseCodeIsUnrecognized = getPotentialIssue(session, "Patient", "phone tel use code", "is unrecognized", "");
    PatientPhoneTelEquipCodeIsDeprecated = getPotentialIssue(session, "Patient", "phone tel equip code", "is deprecated", "");
    PatientPhoneTelEquipCodeIsIgnored = getPotentialIssue(session, "Patient", "phone tel equip code", "is ignored", "");
    PatientPhoneTelEquipCodeIsInvalid = getPotentialIssue(session, "Patient", "phone tel equip code", "is invalid", "");
    PatientPhoneTelEquipCodeIsMissing = getPotentialIssue(session, "Patient", "phone tel equip code", "is missing", "");
    PatientPhoneTelEquipCodeIsUnrecognized = getPotentialIssue(session, "Patient", "phone tel equip code", "is unrecognized", "");
    PatientPrimaryFacilityIdIsDeprecated = getPotentialIssue(session, "Patient", "primary facility id", "is deprecated", "");
    PatientPrimaryFacilityIdIsIgnored = getPotentialIssue(session, "Patient", "primary facility id", "is ignored", "");
    PatientPrimaryFacilityIdIsInvalid = getPotentialIssue(session, "Patient", "primary facility id", "is invalid", "");
    PatientPrimaryFacilityIdIsMissing = getPotentialIssue(session, "Patient", "primary facility id", "is missing", "");
    PatientPrimaryFacilityIdIsUnrecognized = getPotentialIssue(session, "Patient", "primary facility id", "is unrecognized", "");
    PatientPrimaryFacilityNameIsMissing = getPotentialIssue(session, "Patient", "primary facility name", "is missing", "");
    PatientPrimaryLanguageIsDeprecated = getPotentialIssue(session, "Patient", "primary language", "is deprecated", "");
    PatientPrimaryLanguageIsIgnored = getPotentialIssue(session, "Patient", "primary language", "is ignored", "");
    PatientPrimaryLanguageIsInvalid = getPotentialIssue(session, "Patient", "primary language", "is invalid", "");
    PatientPrimaryLanguageIsMissing = getPotentialIssue(session, "Patient", "primary language", "is missing", "");
    PatientPrimaryLanguageIsUnrecognized = getPotentialIssue(session, "Patient", "primary language", "is unrecognized", "");
    PatientPrimaryPhysicianIdIsDeprecated = getPotentialIssue(session, "Patient", "primary physician id", "is deprecated", "");
    PatientPrimaryPhysicianIdIsIgnored = getPotentialIssue(session, "Patient", "primary physician id", "is ignored", "");
    PatientPrimaryPhysicianIdIsInvalid = getPotentialIssue(session, "Patient", "primary physician id", "is invalid", "");
    PatientPrimaryPhysicianIdIsMissing = getPotentialIssue(session, "Patient", "primary physician id", "is missing", "");
    PatientPrimaryPhysicianIdIsUnrecognized = getPotentialIssue(session, "Patient", "primary physician id", "is unrecognized", "");
    PatientPrimaryPhysicianNameIsMissing = getPotentialIssue(session, "Patient", "primary physician name", "is missing", "");
    PatientProtectionIndicatorIsDeprecated = getPotentialIssue(session, "Patient", "protection indicator", "is deprecated", "");
    PatientProtectionIndicatorIsIgnored = getPotentialIssue(session, "Patient", "protection indicator", "is ignored", "");
    PatientProtectionIndicatorIsInvalid = getPotentialIssue(session, "Patient", "protection indicator", "is invalid", "");
    PatientProtectionIndicatorIsMissing = getPotentialIssue(session, "Patient", "protection indicator", "is missing", "");
    PatientProtectionIndicatorIsUnrecognized = getPotentialIssue(session, "Patient", "protection indicator", "is unrecognized", "");
    PatientProtectionIndicatorIsValuedAsNo = getPotentialIssue(session, "Patient", "protection indicator", "is valued as", "no");
    PatientProtectionIndicatorIsValuedAsYes = getPotentialIssue(session, "Patient", "protection indicator", "is valued as", "yes");
    PatientPublicityCodeIsDeprecated = getPotentialIssue(session, "Patient", "publicity code", "is deprecated", "");
    PatientPublicityCodeIsIgnored = getPotentialIssue(session, "Patient", "publicity code", "is ignored", "");
    PatientPublicityCodeIsInvalid = getPotentialIssue(session, "Patient", "publicity code", "is invalid", "");
    PatientPublicityCodeIsMissing = getPotentialIssue(session, "Patient", "publicity code", "is missing", "");
    PatientPublicityCodeIsUnrecognized = getPotentialIssue(session, "Patient", "publicity code", "is unrecognized", "");
    PatientRaceIsDeprecated = getPotentialIssue(session, "Patient", "race", "is deprecated", "");
    PatientRaceIsIgnored = getPotentialIssue(session, "Patient", "race", "is ignored", "");
    PatientRaceIsInvalid = getPotentialIssue(session, "Patient", "race", "is invalid", "");
    PatientRaceIsMissing = getPotentialIssue(session, "Patient", "race", "is missing", "");
    PatientRaceIsUnrecognized = getPotentialIssue(session, "Patient", "race", "is unrecognized", "");
    PatientRegistryIdIsMissing = getPotentialIssue(session, "Patient", "registry id", "is missing", "");
    PatientRegistryIdIsUnrecognized = getPotentialIssue(session, "Patient", "registry id", "is unrecognized", "");
    PatientRegistryStatusIsDeprecated = getPotentialIssue(session, "Patient", "registry status", "is deprecated", "");
    PatientRegistryStatusIsIgnored = getPotentialIssue(session, "Patient", "registry status", "is ignored", "");
    PatientRegistryStatusIsInvalid = getPotentialIssue(session, "Patient", "registry status", "is invalid", "");
    PatientRegistryStatusIsMissing = getPotentialIssue(session, "Patient", "registry status", "is missing", "");
    PatientRegistryStatusIsUnrecognized = getPotentialIssue(session, "Patient", "registry status", "is unrecognized", "");
    PatientSsnIsInvalid = getPotentialIssue(session, "Patient", "SSN", "is invalid", "");
    PatientSsnIsMissing = getPotentialIssue(session, "Patient", "SSN", "is missing", "");
    PatientSubmitterIdIsMissing = getPotentialIssue(session, "Patient", "submitter id", "is missing", "");
    PatientSubmitterIdAuthorityIsMissing = getPotentialIssue(session, "Patient", "submitter id authority", "is missing", "");
    PatientSubmitterIdTypeCodeIsMissing = getPotentialIssue(session, "Patient", "submitter id type code", "is missing", "");
    PatientSubmitterIdTypeCodeIsDeprecated = getPotentialIssue(session, "Patient", "submitter id type code", "is deprecated", "");
    PatientSubmitterIdTypeCodeIsInvalid = getPotentialIssue(session, "Patient", "submitter id type code", "is invalid", "");
    PatientSubmitterIdTypeCodeIsUnrecognized = getPotentialIssue(session, "Patient", "submitter id type code", "is unrecognized", "");
    PatientSubmitterIdTypeCodeIsIgnored = getPotentialIssue(session, "Patient", "submitter id type code", "is ignored", "");
    PatientSystemCreationDateIsMissing = getPotentialIssue(session, "Patient", "system creation date", "is missing", "");
    PatientSystemCreationDateIsInvalid = getPotentialIssue(session, "Patient", "system creation date", "is invalid", "");
    PatientSystemCreationDateIsBeforeBirth = getPotentialIssue(session, "Patient", "system creation date", "is before birth", "");
    PatientSystemCreationDateIsInFuture = getPotentialIssue(session, "Patient", "system creation date", "is in future", "");
    PatientVfcEffectiveDateIsBeforeBirth = getPotentialIssue(session, "Patient", "VFC effective date", "is before birth", "");
    PatientVfcEffectiveDateIsInFuture = getPotentialIssue(session, "Patient", "VFC effective date", "is in future", "");
    PatientVfcEffectiveDateIsInvalid = getPotentialIssue(session, "Patient", "VFC effective date", "is invalid", "");
    PatientVfcEffectiveDateIsMissing = getPotentialIssue(session, "Patient", "VFC effective date", "is missing", "");
    PatientVfcStatusIsDeprecated = getPotentialIssue(session, "Patient", "VFC status", "is deprecated", "");
    PatientVfcStatusIsIgnored = getPotentialIssue(session, "Patient", "VFC status", "is ignored", "");
    PatientVfcStatusIsInvalid = getPotentialIssue(session, "Patient", "VFC status", "is invalid", "");
    PatientVfcStatusIsMissing = getPotentialIssue(session, "Patient", "VFC status", "is missing", "");
    PatientVfcStatusIsUnrecognized = getPotentialIssue(session, "Patient", "VFC status", "is unrecognized", "");
    PatientWicIdIsInvalid = getPotentialIssue(session, "Patient", "WIC id", "is invalid", "");
    PatientWicIdIsMissing = getPotentialIssue(session, "Patient", "WIC id", "is missing", "");
    VaccinationActionCodeIsDeprecated = getPotentialIssue(session, "Vaccination", "action code", "is deprecated", "");
    VaccinationActionCodeIsIgnored = getPotentialIssue(session, "Vaccination", "action code", "is ignored", "");
    VaccinationActionCodeIsInvalid = getPotentialIssue(session, "Vaccination", "action code", "is invalid", "");
    VaccinationActionCodeIsMissing = getPotentialIssue(session, "Vaccination", "action code", "is missing", "");
    VaccinationActionCodeIsUnrecognized = getPotentialIssue(session, "Vaccination", "action code", "is unrecognized", "");
    VaccinationActionCodeIsValuedAsAdd = getPotentialIssue(session, "Vaccination", "action code", "is valued as", "add");
    VaccinationActionCodeIsValuedAsAddOrUpdate = getPotentialIssue(session, "Vaccination", "action code", "is valued as", "add or update");
    VaccinationActionCodeIsValuedAsDelete = getPotentialIssue(session, "Vaccination", "action code", "is valued as", "delete");
    VaccinationActionCodeIsValuedAsUpdate = getPotentialIssue(session, "Vaccination", "action code", "is valued as", "update");
    VaccinationAdminCodeIsDeprecated = getPotentialIssue(session, "Vaccination", "admin code", "is deprecated", "");
    VaccinationAdminCodeIsIgnored = getPotentialIssue(session, "Vaccination", "admin code", "is ignored", "");
    VaccinationAdminCodeIsInvalid = getPotentialIssue(session, "Vaccination", "admin code", "is invalid", "");
    VaccinationAdminCodeIsInvalidForDateAdministered = getPotentialIssue(session, "Vaccination", "admin code", "is invalid for date administered", "");
    VaccinationAdminCodeIsMissing = getPotentialIssue(session, "Vaccination", "admin code", "is missing", "");
    VaccinationAdminCodeIsNotSpecific = getPotentialIssue(session, "Vaccination", "admin code", "is not specific", "");
    VaccinationAdminCodeIsNotVaccine = getPotentialIssue(session, "Vaccination", "admin code", "is not vaccine", "");
    VaccinationAdminCodeIsUnexpectedForDateAdministered = getPotentialIssue(session, "Vaccination", "admin code", "is unexpected for date administered", "");
    VaccinationAdminCodeIsUnrecognized = getPotentialIssue(session, "Vaccination", "admin code", "is unrecognized", "");
    VaccinationAdminCodeIsValuedAsNotAdministered = getPotentialIssue(session, "Vaccination", "admin code", "is valued as", "not administered");
    VaccinationAdminCodeIsValuedAsUnknown = getPotentialIssue(session, "Vaccination", "admin code", "is valued as", "unknown");
    VaccinationAdminCodeMayBeVariationOfPreviouslyReportedCodes = getPotentialIssue(session, "Vaccination", "admin code", "may be variation of previously reported codes", "");
    VaccinationAdminCodeTableIsMissing = getPotentialIssue(session, "Vaccination", "admin code table", "is missing", "");
    VaccinationAdminCodeTableIsInvalid = getPotentialIssue(session, "Vaccination", "admin code table", "is invalid", "");
    VaccinationAdminDateIsAfterLotExpirationDate = getPotentialIssue(session, "Vaccination", "admin date", "is after lot expiration date", "");
    VaccinationAdminDateIsAfterMessageSubmitted = getPotentialIssue(session, "Vaccination", "admin date", "is after message submitted", "");
    VaccinationAdminDateIsAfterPatientDeathDate = getPotentialIssue(session, "Vaccination", "admin date", "is after patient death date", "");
    VaccinationAdminDateIsAfterSystemEntryDate = getPotentialIssue(session, "Vaccination", "admin date", "is after system entry date", "");
    VaccinationAdminDateIsBeforeBirth = getPotentialIssue(session, "Vaccination", "admin date", "is before birth", "");
    VaccinationAdminDateIsBeforeOrAfterExpectedVaccineUsageRange = getPotentialIssue(session, "Vaccination", "admin date", "is before or after expected vaccine usage range", "");
    VaccinationAdminDateIsBeforeOrAfterLicensedVaccineRange = getPotentialIssue(session, "Vaccination", "admin date", "is before or after licensed vaccine range", "");
    VaccinationAdminDateIsBeforeOrAfterWhenExpectedForPatientAge = getPotentialIssue(session, "Vaccination", "admin date", "is before or after when expected for patient age", "");
    VaccinationAdminDateIsBeforeOrAfterWhenValidForPatientAge = getPotentialIssue(session, "Vaccination", "admin date", "is before or after when valid for patient age", "");
    VaccinationAdminDateIsInvalid = getPotentialIssue(session, "Vaccination", "admin date", "is invalid", "");
    VaccinationAdminDateIsMissing = getPotentialIssue(session, "Vaccination", "admin date", "is missing", "");
    VaccinationAdminDateIsOn15ThDayOfMonth = getPotentialIssue(session, "Vaccination", "admin date", "is on 15th day of month", "");
    VaccinationAdminDateIsOnFirstDayOfMonth = getPotentialIssue(session, "Vaccination", "admin date", "is on first day of month", "");
    VaccinationAdminDateIsOnLastDayOfMonth = getPotentialIssue(session, "Vaccination", "admin date", "is on last day of month", "");
    VaccinationAdminDateIsReportedLate = getPotentialIssue(session, "Vaccination", "admin date", "is reported late", "");
    VaccinationAdminDateEndIsDifferentFromStartDate = getPotentialIssue(session, "Vaccination", "admin date end", "is different from start date", "");
    VaccinationAdminDateEndIsMissing = getPotentialIssue(session, "Vaccination", "admin date end", "is missing", "");
    VaccinationAdministeredAmountIsInvalid = getPotentialIssue(session, "Vaccination", "administered amount", "is invalid", "");
    VaccinationAdministeredAmountIsMissing = getPotentialIssue(session, "Vaccination", "administered amount", "is missing", "");
    VaccinationAdministeredAmountIsValuedAsZero = getPotentialIssue(session, "Vaccination", "administered amount", "is valued as", "zero");
    VaccinationAdministeredAmountIsValuedAsUnknown = getPotentialIssue(session, "Vaccination", "administered amount", "is valued as", "unknown");
    VaccinationAdministeredUnitIsDeprecated = getPotentialIssue(session, "Vaccination", "administered unit", "is deprecated", "");
    VaccinationAdministeredUnitIsIgnored = getPotentialIssue(session, "Vaccination", "administered unit", "is ignored", "");
    VaccinationAdministeredUnitIsInvalid = getPotentialIssue(session, "Vaccination", "administered unit", "is invalid", "");
    VaccinationAdministeredUnitIsMissing = getPotentialIssue(session, "Vaccination", "administered unit", "is missing", "");
    VaccinationAdministeredUnitIsUnrecognized = getPotentialIssue(session, "Vaccination", "administered unit", "is unrecognized", "");
    VaccinationBodyRouteIsDeprecated = getPotentialIssue(session, "Vaccination", "body route", "is deprecated", "");
    VaccinationBodyRouteIsIgnored = getPotentialIssue(session, "Vaccination", "body route", "is ignored", "");
    VaccinationBodyRouteIsInvalid = getPotentialIssue(session, "Vaccination", "body route", "is invalid", "");
    VaccinationBodyRouteIsInvalidForVaccineIndicated = getPotentialIssue(session, "Vaccination", "body route", "is invalid for vaccine indicated", "");
    VaccinationBodyRouteIsInvalidForBodySiteIndicated = getPotentialIssue(session, "Vaccination", "body route", "is invalid for body site indicated", "");
    VaccinationBodyRouteIsMissing = getPotentialIssue(session, "Vaccination", "body route", "is missing", "");
    VaccinationBodyRouteIsUnrecognized = getPotentialIssue(session, "Vaccination", "body route", "is unrecognized", "");
    VaccinationBodySiteIsDeprecated = getPotentialIssue(session, "Vaccination", "body site", "is deprecated", "");
    VaccinationBodySiteIsIgnored = getPotentialIssue(session, "Vaccination", "body site", "is ignored", "");
    VaccinationBodySiteIsInvalid = getPotentialIssue(session, "Vaccination", "body site", "is invalid", "");
    VaccinationBodySiteIsInvalidForVaccineIndicated = getPotentialIssue(session, "Vaccination", "body site", "is invalid for vaccine indicated", "");
    VaccinationBodySiteIsMissing = getPotentialIssue(session, "Vaccination", "body site", "is missing", "");
    VaccinationBodySiteIsUnrecognized = getPotentialIssue(session, "Vaccination", "body site", "is unrecognized", "");
    VaccinationCompletionStatusIsDeprecated = getPotentialIssue(session, "Vaccination", "completion status", "is deprecated", "");
    VaccinationCompletionStatusIsIgnored = getPotentialIssue(session, "Vaccination", "completion status", "is ignored", "");
    VaccinationCompletionStatusIsInvalid = getPotentialIssue(session, "Vaccination", "completion status", "is invalid", "");
    VaccinationCompletionStatusIsMissing = getPotentialIssue(session, "Vaccination", "completion status", "is missing", "");
    VaccinationCompletionStatusIsUnrecognized = getPotentialIssue(session, "Vaccination", "completion status", "is unrecognized", "");
    VaccinationCompletionStatusIsValuedAsCompleted = getPotentialIssue(session, "Vaccination", "completion status", "is valued as", "completed");
    VaccinationCompletionStatusIsValuedAsNotAdministered = getPotentialIssue(session, "Vaccination", "completion status", "is valued as", "not administered");
    VaccinationCompletionStatusIsValuedAsPartiallyAdministered = getPotentialIssue(session, "Vaccination", "completion status", "is valued as", "partially administered");
    VaccinationCompletionStatusIsValuedAsRefused = getPotentialIssue(session, "Vaccination", "completion status", "is valued as", "refused");
    VaccinationConfidentialityCodeIsDeprecated = getPotentialIssue(session, "Vaccination", "confidentiality code", "is deprecated", "");
    VaccinationConfidentialityCodeIsIgnored = getPotentialIssue(session, "Vaccination", "confidentiality code", "is ignored", "");
    VaccinationConfidentialityCodeIsInvalid = getPotentialIssue(session, "Vaccination", "confidentiality code", "is invalid", "");
    VaccinationConfidentialityCodeIsMissing = getPotentialIssue(session, "Vaccination", "confidentiality code", "is missing", "");
    VaccinationConfidentialityCodeIsUnrecognized = getPotentialIssue(session, "Vaccination", "confidentiality code", "is unrecognized", "");
    VaccinationConfidentialityCodeIsValuedAsRestricted = getPotentialIssue(session, "Vaccination", "confidentiality code", "is valued as", "restricted");
    VaccinationCptCodeIsDeprecated = getPotentialIssue(session, "Vaccination", "CPT code", "is deprecated", "");
    VaccinationCptCodeIsIgnored = getPotentialIssue(session, "Vaccination", "CPT code", "is ignored", "");
    VaccinationCptCodeIsInvalid = getPotentialIssue(session, "Vaccination", "CPT code", "is invalid", "");
    VaccinationCptCodeIsInvalidForDateAdministered = getPotentialIssue(session, "Vaccination", "CPT code", "is invalid for date administered", "");
    VaccinationCptCodeIsMissing = getPotentialIssue(session, "Vaccination", "CPT code", "is missing", "");
    VaccinationCptCodeIsUnexpectedForDateAdministered = getPotentialIssue(session, "Vaccination", "CPT code", "is unexpected for date administered", "");
    VaccinationCptCodeIsUnrecognized = getPotentialIssue(session, "Vaccination", "CPT code", "is unrecognized", "");
    VaccinationCvxCodeIsDeprecated = getPotentialIssue(session, "Vaccination", "CVX code", "is deprecated", "");
    VaccinationCvxCodeIsIgnored = getPotentialIssue(session, "Vaccination", "CVX code", "is ignored", "");
    VaccinationCvxCodeIsInvalid = getPotentialIssue(session, "Vaccination", "CVX code", "is invalid", "");
    VaccinationCvxCodeIsInvalidForDateAdministered = getPotentialIssue(session, "Vaccination", "CVX code", "is invalid for date administered", "");
    VaccinationCvxCodeIsMissing = getPotentialIssue(session, "Vaccination", "CVX code", "is missing", "");
    VaccinationCvxCodeIsUnexpectedForDateAdministered = getPotentialIssue(session, "Vaccination", "CVX code", "is unexpected for date administered", "");
    VaccinationCvxCodeIsUnrecognized = getPotentialIssue(session, "Vaccination", "CVX code", "is unrecognized", "");
    VaccinationCvxCodeAndCptCodeAreInconsistent = getPotentialIssue(session, "Vaccination", "CVX code and CPT code", "are inconsistent", "");
    VaccinationFacilityIdIsDeprecated = getPotentialIssue(session, "Vaccination", "facility id", "is deprecated", "");
    VaccinationFacilityIdIsIgnored = getPotentialIssue(session, "Vaccination", "facility id", "is ignored", "");
    VaccinationFacilityIdIsInvalid = getPotentialIssue(session, "Vaccination", "facility id", "is invalid", "");
    VaccinationFacilityIdIsMissing = getPotentialIssue(session, "Vaccination", "facility id", "is missing", "");
    VaccinationFacilityIdIsUnrecognized = getPotentialIssue(session, "Vaccination", "facility id", "is unrecognized", "");
    VaccinationFacilityNameIsMissing = getPotentialIssue(session, "Vaccination", "facility name", "is missing", "");
    VaccinationFacilityTypeIsDeprecated = getPotentialIssue(session, "Vaccination", "facility type", "is deprecated", "");
    VaccinationFacilityTypeIsIgnored = getPotentialIssue(session, "Vaccination", "facility type", "is ignored", "");
    VaccinationFacilityTypeIsInvalid = getPotentialIssue(session, "Vaccination", "facility type", "is invalid", "");
    VaccinationFacilityTypeIsMissing = getPotentialIssue(session, "Vaccination", "facility type", "is missing", "");
    VaccinationFacilityTypeIsUnrecognized = getPotentialIssue(session, "Vaccination", "facility type", "is unrecognized", "");
    VaccinationFacilityTypeIsValuedAsPublic = getPotentialIssue(session, "Vaccination", "facility type", "is valued as", "public");
    VaccinationFacilityTypeIsValuedAsPrivate = getPotentialIssue(session, "Vaccination", "facility type", "is valued as", "private");
    VaccinationFillerOrderNumberIsDeprecated = getPotentialIssue(session, "Vaccination", "filler order number", "is deprecated", "");
    VaccinationFillerOrderNumberIsIgnored = getPotentialIssue(session, "Vaccination", "filler order number", "is ignored", "");
    VaccinationFillerOrderNumberIsInvalid = getPotentialIssue(session, "Vaccination", "filler order number", "is invalid", "");
    VaccinationFillerOrderNumberIsMissing = getPotentialIssue(session, "Vaccination", "filler order number", "is missing", "");
    VaccinationFillerOrderNumberIsUnrecognized = getPotentialIssue(session, "Vaccination", "filler order number", "is unrecognized", "");
    VaccinationFinancialEligibilityCodeIsDeprecated = getPotentialIssue(session, "Vaccination", "financial eligibility code", "is deprecated", "");
    VaccinationFinancialEligibilityCodeIsIgnored = getPotentialIssue(session, "Vaccination", "financial eligibility code", "is ignored", "");
    VaccinationFinancialEligibilityCodeIsInvalid = getPotentialIssue(session, "Vaccination", "financial eligibility code", "is invalid", "");
    VaccinationFinancialEligibilityCodeIsMissing = getPotentialIssue(session, "Vaccination", "financial eligibility code", "is missing", "");
    VaccinationFinancialEligibilityCodeIsUnrecognized = getPotentialIssue(session, "Vaccination", "financial eligibility code", "is unrecognized", "");
    VaccinationGivenByIsDeprecated = getPotentialIssue(session, "Vaccination", "given by", "is deprecated", "");
    VaccinationGivenByIsIgnored = getPotentialIssue(session, "Vaccination", "given by", "is ignored", "");
    VaccinationGivenByIsInvalid = getPotentialIssue(session, "Vaccination", "given by", "is invalid", "");
    VaccinationGivenByIsMissing = getPotentialIssue(session, "Vaccination", "given by", "is missing", "");
    VaccinationGivenByIsUnrecognized = getPotentialIssue(session, "Vaccination", "given by", "is unrecognized", "");
    VaccinationIdIsMissing = getPotentialIssue(session, "Vaccination", "id", "is missing", "");
    VaccinationIdOfReceiverIsMissing = getPotentialIssue(session, "Vaccination", "id of receiver", "is missing", "");
    VaccinationIdOfReceiverIsUnrecognized = getPotentialIssue(session, "Vaccination", "id of receiver", "is unrecognized", "");
    VaccinationIdOfSenderIsMissing = getPotentialIssue(session, "Vaccination", "id of sender", "is missing", "");
    VaccinationIdOfSenderIsUnrecognized = getPotentialIssue(session, "Vaccination", "id of sender", "is unrecognized", "");
    VaccinationInformationSourceIsAdministeredButAppearsToHistorical = getPotentialIssue(session, "Vaccination", "information source", "is administered but appears to historical", "");
    VaccinationInformationSourceIsDeprecated = getPotentialIssue(session, "Vaccination", "information source", "is deprecated", "");
    VaccinationInformationSourceIsHistoricalButAppearsToBeAdministered = getPotentialIssue(session, "Vaccination", "information source", "is historical but appears to be administered", "");
    VaccinationInformationSourceIsIgnored = getPotentialIssue(session, "Vaccination", "information source", "is ignored", "");
    VaccinationInformationSourceIsInvalid = getPotentialIssue(session, "Vaccination", "information source", "is invalid", "");
    VaccinationInformationSourceIsMissing = getPotentialIssue(session, "Vaccination", "information source", "is missing", "");
    VaccinationInformationSourceIsUnrecognized = getPotentialIssue(session, "Vaccination", "information source", "is unrecognized", "");
    VaccinationInformationSourceIsValuedAsAdministered = getPotentialIssue(session, "Vaccination", "information source", "is valued as", "administered");
    VaccinationInformationSourceIsValuedAsHistorical = getPotentialIssue(session, "Vaccination", "information source", "is valued as", "historical");
    VaccinationVisIsMissing = getPotentialIssue(session, "Vaccination", "VIS", "is missing", "");
    VaccinationVisIsUnrecognized = getPotentialIssue(session, "Vaccination", "VIS", "is unrecognized", "");
    VaccinationVisIsDeprecated = getPotentialIssue(session, "Vaccination", "VIS", "is deprecated", "");
    VaccinationVisCvxCodeIsDeprecated = getPotentialIssue(session, "Vaccination", "VIS CVX Code", "is deprecated", "");
    VaccinationVisCvxCodeIsIgnored = getPotentialIssue(session, "Vaccination", "VIS CVX Code", "is ignored", "");
    VaccinationVisCvxCodeIsInvalid = getPotentialIssue(session, "Vaccination", "VIS CVX Code", "is invalid", "");
    VaccinationVisCvxCodeIsMissing = getPotentialIssue(session, "Vaccination", "VIS CVX Code", "is missing", "");
    VaccinationVisCvxCodeIsUnrecognized = getPotentialIssue(session, "Vaccination", "VIS CVX Code", "is unrecognized", "");
    VaccinationVisDocumentTypeIsDeprecated = getPotentialIssue(session, "Vaccination", "VIS document type", "is deprecated", "");
    VaccinationVisDocumentTypeIsIgnored = getPotentialIssue(session, "Vaccination", "VIS document type", "is ignored", "");
    VaccinationVisDocumentTypeIsIncorrect = getPotentialIssue(session, "Vaccination", "VIS document type", "is incorrect", "");
    VaccinationVisDocumentTypeIsInvalid = getPotentialIssue(session, "Vaccination", "VIS document type", "is invalid", "");
    VaccinationVisDocumentTypeIsMissing = getPotentialIssue(session, "Vaccination", "VIS document type", "is missing", "");
    VaccinationVisDocumentTypeIsUnrecognized = getPotentialIssue(session, "Vaccination", "VIS document type", "is unrecognized", "");
    VaccinationVisDocumentTypeIsOutOfDate = getPotentialIssue(session, "Vaccination", "VIS document type", "is out-of-date", "");
    VaccinationVisPublishedDateIsInvalid = getPotentialIssue(session, "Vaccination", "VIS published date", "is invalid", "");
    VaccinationVisPublishedDateIsMissing = getPotentialIssue(session, "Vaccination", "VIS published date", "is missing", "");
    VaccinationVisPublishedDateIsUnrecognized = getPotentialIssue(session, "Vaccination", "VIS published date", "is unrecognized", "");
    VaccinationVisPublishedDateIsInFuture = getPotentialIssue(session, "Vaccination", "VIS published date", "is in future", "");
    VaccinationVisPresentedDateIsInvalid = getPotentialIssue(session, "Vaccination", "VIS presented date", "is invalid", "");
    VaccinationVisPresentedDateIsMissing = getPotentialIssue(session, "Vaccination", "VIS presented date", "is missing", "");
    VaccinationVisPresentedDateIsNotAdminDate = getPotentialIssue(session, "Vaccination", "VIS presented date", "is not admin date", "");
    VaccinationVisPresentedDateIsBeforePublishedDate = getPotentialIssue(session, "Vaccination", "VIS presented date", "is before published date", "");
    VaccinationVisPresentedDateIsAfterAdminDate = getPotentialIssue(session, "Vaccination", "VIS presented date", "is after admin date", "");
    VaccinationLotExpirationDateIsInvalid = getPotentialIssue(session, "Vaccination", "lot expiration date", "is invalid", "");
    VaccinationLotExpirationDateIsMissing = getPotentialIssue(session, "Vaccination", "lot expiration date", "is missing", "");
    VaccinationLotNumberIsInvalid = getPotentialIssue(session, "Vaccination", "lot number", "is invalid", "");
    VaccinationLotNumberIsMissing = getPotentialIssue(session, "Vaccination", "lot number", "is missing", "");
    VaccinationManufacturerCodeIsDeprecated = getPotentialIssue(session, "Vaccination", "manufacturer code", "is deprecated", "");
    VaccinationManufacturerCodeIsIgnored = getPotentialIssue(session, "Vaccination", "manufacturer code", "is ignored", "");
    VaccinationManufacturerCodeIsInvalid = getPotentialIssue(session, "Vaccination", "manufacturer code", "is invalid", "");
    VaccinationManufacturerCodeIsInvalidForDateAdministered = getPotentialIssue(session, "Vaccination", "manufacturer code", "is invalid for date administered", "");
    VaccinationManufacturerCodeIsMissing = getPotentialIssue(session, "Vaccination", "manufacturer code", "is missing", "");
    VaccinationManufacturerCodeIsUnexpectedForDateAdministered = getPotentialIssue(session, "Vaccination", "manufacturer code", "is unexpected for date administered", "");
    VaccinationManufacturerCodeIsUnrecognized = getPotentialIssue(session, "Vaccination", "manufacturer code", "is unrecognized", "");
    VaccinationOrderControlCodeIsDeprecated = getPotentialIssue(session, "Vaccination", "order control code", "is deprecated", "");
    VaccinationOrderControlCodeIsIgnored = getPotentialIssue(session, "Vaccination", "order control code", "is ignored", "");
    VaccinationOrderControlCodeIsInvalid = getPotentialIssue(session, "Vaccination", "order control code", "is invalid", "");
    VaccinationOrderControlCodeIsMissing = getPotentialIssue(session, "Vaccination", "order control code", "is missing", "");
    VaccinationOrderControlCodeIsUnrecognized = getPotentialIssue(session, "Vaccination", "order control code", "is unrecognized", "");
    VaccinationOrderFacilityIdIsDeprecated = getPotentialIssue(session, "Vaccination", "order facility id", "is deprecated", "");
    VaccinationOrderFacilityIdIsIgnored = getPotentialIssue(session, "Vaccination", "order facility id", "is ignored", "");
    VaccinationOrderFacilityIdIsInvalid = getPotentialIssue(session, "Vaccination", "order facility id", "is invalid", "");
    VaccinationOrderFacilityIdIsMissing = getPotentialIssue(session, "Vaccination", "order facility id", "is missing", "");
    VaccinationOrderFacilityIdIsUnrecognized = getPotentialIssue(session, "Vaccination", "order facility id", "is unrecognized", "");
    VaccinationOrderFacilityNameIsMissing = getPotentialIssue(session, "Vaccination", "order facility name", "is missing", "");
    VaccinationOrderedByIsDeprecated = getPotentialIssue(session, "Vaccination", "ordered by", "is deprecated", "");
    VaccinationOrderedByIsIgnored = getPotentialIssue(session, "Vaccination", "ordered by", "is ignored", "");
    VaccinationOrderedByIsInvalid = getPotentialIssue(session, "Vaccination", "ordered by", "is invalid", "");
    VaccinationOrderedByIsMissing = getPotentialIssue(session, "Vaccination", "ordered by", "is missing", "");
    VaccinationOrderedByIsUnrecognized = getPotentialIssue(session, "Vaccination", "ordered by", "is unrecognized", "");
    VaccinationPlacerOrderNumberIsDeprecated = getPotentialIssue(session, "Vaccination", "placer order number", "is deprecated", "");
    VaccinationPlacerOrderNumberIsIgnored = getPotentialIssue(session, "Vaccination", "placer order number", "is ignored", "");
    VaccinationPlacerOrderNumberIsInvalid = getPotentialIssue(session, "Vaccination", "placer order number", "is invalid", "");
    VaccinationPlacerOrderNumberIsMissing = getPotentialIssue(session, "Vaccination", "placer order number", "is missing", "");
    VaccinationPlacerOrderNumberIsUnrecognized = getPotentialIssue(session, "Vaccination", "placer order number", "is unrecognized", "");
    VaccinationProductIsDeprecated = getPotentialIssue(session, "Vaccination", "product", "is deprecated", "");
    VaccinationProductIsInvalid = getPotentialIssue(session, "Vaccination", "product", "is invalid", "");
    VaccinationProductIsInvalidForDateAdministered = getPotentialIssue(session, "Vaccination", "product", "is invalid for date administered", "");
    VaccinationProductIsMissing = getPotentialIssue(session, "Vaccination", "product", "is missing", "");
    VaccinationProductIsUnexpectedForDateAdministered = getPotentialIssue(session, "Vaccination", "product", "is unexpected for date administered", "");
    VaccinationProductIsUnrecognized = getPotentialIssue(session, "Vaccination", "product", "is unrecognized", "");
    VaccinationRecordedByIsDeprecated = getPotentialIssue(session, "Vaccination", "recorded by", "is deprecated", "");
    VaccinationRecordedByIsIgnored = getPotentialIssue(session, "Vaccination", "recorded by", "is ignored", "");
    VaccinationRecordedByIsInvalid = getPotentialIssue(session, "Vaccination", "recorded by", "is invalid", "");
    VaccinationRecordedByIsMissing = getPotentialIssue(session, "Vaccination", "recorded by", "is missing", "");
    VaccinationRecordedByIsUnrecognized = getPotentialIssue(session, "Vaccination", "recorded by", "is unrecognized", "");
    VaccinationRefusalReasonConflictsCompletionStatus = getPotentialIssue(session, "Vaccination", "refusal reason", "conflicts completion status", "");
    VaccinationRefusalReasonIsDeprecated = getPotentialIssue(session, "Vaccination", "refusal reason", "is deprecated", "");
    VaccinationRefusalReasonIsIgnored = getPotentialIssue(session, "Vaccination", "refusal reason", "is ignored", "");
    VaccinationRefusalReasonIsInvalid = getPotentialIssue(session, "Vaccination", "refusal reason", "is invalid", "");
    VaccinationRefusalReasonIsMissing = getPotentialIssue(session, "Vaccination", "refusal reason", "is missing", "");
    VaccinationRefusalReasonIsUnrecognized = getPotentialIssue(session, "Vaccination", "refusal reason", "is unrecognized", "");
    VaccinationSystemEntryTimeIsInFuture = getPotentialIssue(session, "Vaccination", "system entry time", "is in future", "");
    VaccinationSystemEntryTimeIsInvalid = getPotentialIssue(session, "Vaccination", "system entry time", "is invalid", "");
    VaccinationSystemEntryTimeIsMissing = getPotentialIssue(session, "Vaccination", "system entry time", "is missing", "");
    VaccinationTradeNameIsDeprecated = getPotentialIssue(session, "Vaccination", "trade name", "is deprecated", "");
    VaccinationTradeNameIsIgnored = getPotentialIssue(session, "Vaccination", "trade name", "is ignored", "");
    VaccinationTradeNameIsInvalid = getPotentialIssue(session, "Vaccination", "trade name", "is invalid", "");
    VaccinationTradeNameIsMissing = getPotentialIssue(session, "Vaccination", "trade name", "is missing", "");
    VaccinationTradeNameIsUnrecognized = getPotentialIssue(session, "Vaccination", "trade name", "is unrecognized", "");
    VaccinationTradeNameAndVaccineAreInconsistent = getPotentialIssue(session, "Vaccination", "trade name and vaccine", "are inconsistent", "");
    VaccinationTradeNameAndManufacturerAreInconsistent = getPotentialIssue(session, "Vaccination", "trade name and manufacturer", "are inconsistent", "");
    VaccinationValidityCodeIsInvalid = getPotentialIssue(session, "Vaccination", "validity code", "is invalid", "");
    VaccinationValidityCodeIsDeprecated = getPotentialIssue(session, "Vaccination", "validity code", "is deprecated", "");
    VaccinationValidityCodeIsIgnored = getPotentialIssue(session, "Vaccination", "validity code", "is ignored", "");
    VaccinationValidityCodeIsMissing = getPotentialIssue(session, "Vaccination", "validity code", "is missing", "");
    VaccinationValidityCodeIsUnrecognized = getPotentialIssue(session, "Vaccination", "validity code", "is unrecognized", "");
    VaccinationValidityCodeIsValuedAsValid = getPotentialIssue(session, "Vaccination", "validity code", "is valued as", "valid");
    VaccinationValidityCodeIsValuedAsInvalid = getPotentialIssue(session, "Vaccination", "validity code", "is valued as", "invalid");

    
    
    addToFieldIssueMap(Field.GENERAL_AUTHORIZATION, GeneralAuthorizationException);
    addToFieldIssueMap(Field.GENERAL_CONFIGURATION, GeneralConfigurationException);
    addToFieldIssueMap(Field.GENERAL_PARSE, GeneralParseException);
    addToFieldIssueMap(Field.GENERAL_PROCESSING, GeneralProcessingException);
    addToFieldIssueMap(Field.HL7_SEGMENT, Hl7SegmentIsUnrecognized);
    addToFieldIssueMap(Field.HL7_SEGMENT, Hl7SegmentIsInvalid);
    addToFieldIssueMap(Field.HL7_SEGMENTS, Hl7SegmentsOutOfOrder);
    addToFieldIssueMap(Field.HL7_MSH_ACCEPT_ACK_TYPE, Hl7MshAcceptAckTypeIsDeprecated);
    addToFieldIssueMap(Field.HL7_MSH_ACCEPT_ACK_TYPE, Hl7MshAcceptAckTypeIsIgnored);
    addToFieldIssueMap(Field.HL7_MSH_ACCEPT_ACK_TYPE, Hl7MshAcceptAckTypeIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_ACCEPT_ACK_TYPE, Hl7MshAcceptAckTypeIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_ACCEPT_ACK_TYPE, Hl7MshAcceptAckTypeIsUnrecognized);
    addToFieldIssueMap(Field.HL7_MSH_ACCEPT_ACK_TYPE, Hl7MshAcceptAckTypeIsValuedAsAlways);
    addToFieldIssueMap(Field.HL7_MSH_ACCEPT_ACK_TYPE, Hl7MshAcceptAckTypeIsValuedAsNever);
    addToFieldIssueMap(Field.HL7_MSH_ACCEPT_ACK_TYPE, Hl7MshAcceptAckTypeIsValuedAsOnlyOnErrors);
    addToFieldIssueMap(Field.HL7_MSH_ALT_CHARACTER_SET, Hl7MshAltCharacterSetIsDeprecated);
    addToFieldIssueMap(Field.HL7_MSH_ALT_CHARACTER_SET, Hl7MshAltCharacterSetIsIgnored);
    addToFieldIssueMap(Field.HL7_MSH_ALT_CHARACTER_SET, Hl7MshAltCharacterSetIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_ALT_CHARACTER_SET, Hl7MshAltCharacterSetIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_ALT_CHARACTER_SET, Hl7MshAltCharacterSetIsUnrecognized);
    addToFieldIssueMap(Field.HL7_MSH_APP_ACK_TYPE, Hl7MshAppAckTypeIsDeprecated);
    addToFieldIssueMap(Field.HL7_MSH_APP_ACK_TYPE, Hl7MshAppAckTypeIsIgnored);
    addToFieldIssueMap(Field.HL7_MSH_APP_ACK_TYPE, Hl7MshAppAckTypeIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_APP_ACK_TYPE, Hl7MshAppAckTypeIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_APP_ACK_TYPE, Hl7MshAppAckTypeIsUnrecognized);
    addToFieldIssueMap(Field.HL7_MSH_APP_ACK_TYPE, Hl7MshAppAckTypeIsValuedAsAlways);
    addToFieldIssueMap(Field.HL7_MSH_APP_ACK_TYPE, Hl7MshAppAckTypeIsValuedAsNever);
    addToFieldIssueMap(Field.HL7_MSH_APP_ACK_TYPE, Hl7MshAppAckTypeIsValuedAsOnlyOnErrors);
    addToFieldIssueMap(Field.HL7_MSH_CHARACTER_SET, Hl7MshCharacterSetIsDeprecated);
    addToFieldIssueMap(Field.HL7_MSH_CHARACTER_SET, Hl7MshCharacterSetIsIgnored);
    addToFieldIssueMap(Field.HL7_MSH_CHARACTER_SET, Hl7MshCharacterSetIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_CHARACTER_SET, Hl7MshCharacterSetIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_CHARACTER_SET, Hl7MshCharacterSetIsUnrecognized);
    addToFieldIssueMap(Field.HL7_MSH_COUNTRY_CODE, Hl7MshCountryCodeIsDeprecated);
    addToFieldIssueMap(Field.HL7_MSH_COUNTRY_CODE, Hl7MshCountryCodeIsIgnored);
    addToFieldIssueMap(Field.HL7_MSH_COUNTRY_CODE, Hl7MshCountryCodeIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_COUNTRY_CODE, Hl7MshCountryCodeIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_COUNTRY_CODE, Hl7MshCountryCodeIsUnrecognized);
    addToFieldIssueMap(Field.HL7_MSH_ENCODING_CHARACTER, Hl7MshEncodingCharacterIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_ENCODING_CHARACTER, Hl7MshEncodingCharacterIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_ENCODING_CHARACTER, Hl7MshEncodingCharacterIsNonStandard);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_CONTROL_ID, Hl7MshMessageControlIdIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_DATE, Hl7MshMessageDateIsInFuture);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_DATE, Hl7MshMessageDateIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_DATE, Hl7MshMessageDateIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_DATE, Hl7MshMessageDateIsNotPrecise);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_DATE, Hl7MshMessageDateIsMissingTimezone);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_PROFILE_ID, Hl7MshMessageProfileIdIsDeprecated);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_PROFILE_ID, Hl7MshMessageProfileIdIsIgnored);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_PROFILE_ID, Hl7MshMessageProfileIdIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_PROFILE_ID, Hl7MshMessageProfileIdIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_PROFILE_ID, Hl7MshMessageProfileIdIsUnrecognized);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_STRUCTURE, Hl7MshMessageStructureIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_STRUCTURE, Hl7MshMessageStructureIsUnrecognized);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_TRIGGER, Hl7MshMessageTriggerIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_TRIGGER, Hl7MshMessageTriggerIsUnrecognized);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_TRIGGER, Hl7MshMessageTriggerIsUnsupported);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_TYPE, Hl7MshMessageTypeIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_TYPE, Hl7MshMessageTypeIsUnrecognized);
    addToFieldIssueMap(Field.HL7_MSH_MESSAGE_TYPE, Hl7MshMessageTypeIsUnsupported);
    addToFieldIssueMap(Field.HL7_MSH_PROCESSING_ID, Hl7MshProcessingIdIsDeprecated);
    addToFieldIssueMap(Field.HL7_MSH_PROCESSING_ID, Hl7MshProcessingIdIsIgnored);
    addToFieldIssueMap(Field.HL7_MSH_PROCESSING_ID, Hl7MshProcessingIdIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_PROCESSING_ID, Hl7MshProcessingIdIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_PROCESSING_ID, Hl7MshProcessingIdIsUnrecognized);
    addToFieldIssueMap(Field.HL7_MSH_PROCESSING_ID, Hl7MshProcessingIdIsUnsupported);
    addToFieldIssueMap(Field.HL7_MSH_PROCESSING_ID, Hl7MshProcessingIdIsValuedAsDebug);
    addToFieldIssueMap(Field.HL7_MSH_PROCESSING_ID, Hl7MshProcessingIdIsValuedAsProduction);
    addToFieldIssueMap(Field.HL7_MSH_PROCESSING_ID, Hl7MshProcessingIdIsValuedAsTraining);
    addToFieldIssueMap(Field.HL7_MSH_RECEIVING_APPLICATION, Hl7MshReceivingApplicationIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_RECEIVING_APPLICATION, Hl7MshReceivingApplicationIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_RECEIVING_FACILITY, Hl7MshReceivingFacilityIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_RECEIVING_FACILITY, Hl7MshReceivingFacilityIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_SEGMENT, Hl7MshSegmentIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_SENDING_APPLICATION, Hl7MshSendingApplicationIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_SENDING_APPLICATION, Hl7MshSendingApplicationIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_SENDING_FACILITY, Hl7MshSendingFacilityIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_SENDING_FACILITY, Hl7MshSendingFacilityIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_VERSION, Hl7MshVersionIsMissing);
    addToFieldIssueMap(Field.HL7_MSH_VERSION, Hl7MshVersionIsUnrecognized);
    addToFieldIssueMap(Field.HL7_MSH_VERSION, Hl7MshVersionIsInvalid);
    addToFieldIssueMap(Field.HL7_MSH_VERSION, Hl7MshVersionIsValuedAs2_3_1);
    addToFieldIssueMap(Field.HL7_MSH_VERSION, Hl7MshVersionIsValuedAs2_4);
    addToFieldIssueMap(Field.HL7_MSH_VERSION, Hl7MshVersionIsValuedAs2_5);
    addToFieldIssueMap(Field.HL7_NK1_SEGMENT, Hl7Nk1SegmentIsMissing);
    addToFieldIssueMap(Field.HL7_NK1_SEGMENT, Hl7Nk1SegmentIsRepeated);
    addToFieldIssueMap(Field.HL7_NK1_SET_ID, Hl7Nk1SetIdIsMissing);
    addToFieldIssueMap(Field.HL7_OBX_SEGMENT, Hl7ObxSegmentIsMissing);
    addToFieldIssueMap(Field.HL7_ORC_SEGMENT, Hl7OrcSegmentIsMissing);
    addToFieldIssueMap(Field.HL7_ORC_SEGMENT, Hl7OrcSegmentIsRepeated);
    addToFieldIssueMap(Field.HL7_PD1_SEGMENT, Hl7Pd1SegmentIsMissing);
    addToFieldIssueMap(Field.HL7_PID_SEGMENT, Hl7PidSegmentIsMissing);
    addToFieldIssueMap(Field.HL7_PID_SEGMENT, Hl7PidSegmentIsRepeated);
    addToFieldIssueMap(Field.HL7_PV1_SEGMENT, Hl7Pv1SegmentIsMissing);
    addToFieldIssueMap(Field.HL7_PV1_SEGMENT, Hl7Pv1SegmentIsRepeated);
    addToFieldIssueMap(Field.HL7_RXA_ADMIN_SUB_ID_COUNTER, Hl7RxaAdminSubIdCounterIsMissing);
    addToFieldIssueMap(Field.HL7_RXA_GIVE_SUB_ID, Hl7RxaGiveSubIdIsMissing);
    addToFieldIssueMap(Field.HL7_RXA_SEGMENT, Hl7RxaSegmentIsMissing);
    addToFieldIssueMap(Field.HL7_RXA_SEGMENT, Hl7RxaSegmentIsRepeated);
    addToFieldIssueMap(Field.HL7_RXR_SEGMENT, Hl7RxrSegmentIsMissing);
    addToFieldIssueMap(Field.HL7_RXR_SEGMENT, Hl7RxrSegmentIsRepeated);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS, NextOfKinAddressIsDifferentFromPatientAddress);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS, NextOfKinAddressIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_CITY, NextOfKinAddressCityIsInvalid);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_CITY, NextOfKinAddressCityIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_CITY, NextOfKinAddressCityIsTooShort);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_CITY, NextOfKinAddressCityIsUnexpectedlyShort);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_CITY, NextOfKinAddressCityIsUnexpectedlyLong);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_CITY, NextOfKinAddressCityIsTooLong);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_COUNTRY, NextOfKinAddressCountryIsDeprecated);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_COUNTRY, NextOfKinAddressCountryIsIgnored);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_COUNTRY, NextOfKinAddressCountryIsInvalid);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_COUNTRY, NextOfKinAddressCountryIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_COUNTRY, NextOfKinAddressCountryIsUnrecognized);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_COUNTY, NextOfKinAddressCountyIsDeprecated);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_COUNTY, NextOfKinAddressCountyIsIgnored);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_COUNTY, NextOfKinAddressCountyIsInvalid);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_COUNTY, NextOfKinAddressCountyIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_COUNTY, NextOfKinAddressCountyIsUnrecognized);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_STATE, NextOfKinAddressStateIsDeprecated);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_STATE, NextOfKinAddressStateIsIgnored);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_STATE, NextOfKinAddressStateIsInvalid);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_STATE, NextOfKinAddressStateIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_STATE, NextOfKinAddressStateIsUnrecognized);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_STREET, NextOfKinAddressStreetIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_STREET2, NextOfKinAddressStreet2IsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_TYPE, NextOfKinAddressTypeIsDeprecated);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_TYPE, NextOfKinAddressTypeIsIgnored);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_TYPE, NextOfKinAddressTypeIsInvalid);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_TYPE, NextOfKinAddressTypeIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_TYPE, NextOfKinAddressTypeIsUnrecognized);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_TYPE, NextOfKinAddressTypeIsValuedBadAddress);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_ZIP, NextOfKinAddressZipIsInvalid);
    addToFieldIssueMap(Field.NEXT_OF_KIN_ADDRESS_ZIP, NextOfKinAddressZipIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_NAME, NextOfKinNameIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_NAME_FIRST, NextOfKinNameFirstIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_NAME_FIRST, NextOfKinNameFirstIsTooShort);
    addToFieldIssueMap(Field.NEXT_OF_KIN_NAME_FIRST, NextOfKinNameFirstIsUnexpectedlyShort);
    addToFieldIssueMap(Field.NEXT_OF_KIN_NAME_FIRST, NextOfKinNameFirstIsUnexpectedlyLong);
    addToFieldIssueMap(Field.NEXT_OF_KIN_NAME_FIRST, NextOfKinNameFirstIsTooLong);
    addToFieldIssueMap(Field.NEXT_OF_KIN_NAME_LAST, NextOfKinNameLastIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_NAME_LAST, NextOfKinNameLastIsTooShort);
    addToFieldIssueMap(Field.NEXT_OF_KIN_NAME_LAST, NextOfKinNameLastIsUnexpectedlyShort);
    addToFieldIssueMap(Field.NEXT_OF_KIN_NAME_LAST, NextOfKinNameLastIsUnexpectedlyLong);
    addToFieldIssueMap(Field.NEXT_OF_KIN_NAME_LAST, NextOfKinNameLastIsTooLong);
    addToFieldIssueMap(Field.NEXT_OF_KIN_PHONE_NUMBER, NextOfKinPhoneNumberIsIncomplete);
    addToFieldIssueMap(Field.NEXT_OF_KIN_PHONE_NUMBER, NextOfKinPhoneNumberIsInvalid);
    addToFieldIssueMap(Field.NEXT_OF_KIN_PHONE_NUMBER, NextOfKinPhoneNumberIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_RELATIONSHIP, NextOfKinRelationshipIsDeprecated);
    addToFieldIssueMap(Field.NEXT_OF_KIN_RELATIONSHIP, NextOfKinRelationshipIsIgnored);
    addToFieldIssueMap(Field.NEXT_OF_KIN_RELATIONSHIP, NextOfKinRelationshipIsInvalid);
    addToFieldIssueMap(Field.NEXT_OF_KIN_RELATIONSHIP, NextOfKinRelationshipIsMissing);
    addToFieldIssueMap(Field.NEXT_OF_KIN_RELATIONSHIP, NextOfKinRelationshipIsNotResponsibleParty);
    addToFieldIssueMap(Field.NEXT_OF_KIN_RELATIONSHIP, NextOfKinRelationshipIsUnexpected);
    addToFieldIssueMap(Field.NEXT_OF_KIN_RELATIONSHIP, NextOfKinRelationshipIsUnrecognized);
    addToFieldIssueMap(Field.NEXT_OF_KIN_SSN, NextOfKinSsnIsMissing);
    addToFieldIssueMap(Field.OBSERVATION_VALUE_TYPE, ObservationValueTypeIsDeprecated);
    addToFieldIssueMap(Field.OBSERVATION_VALUE_TYPE, ObservationValueTypeIsIgnored);
    addToFieldIssueMap(Field.OBSERVATION_VALUE_TYPE, ObservationValueTypeIsInvalid);
    addToFieldIssueMap(Field.OBSERVATION_VALUE_TYPE, ObservationValueTypeIsMissing);
    addToFieldIssueMap(Field.OBSERVATION_VALUE_TYPE, ObservationValueTypeIsUnrecognized);
    addToFieldIssueMap(Field.OBSERVATION_IDENTIFIER_CODE, ObservationIdentifierCodeIsDeprecated);
    addToFieldIssueMap(Field.OBSERVATION_IDENTIFIER_CODE, ObservationIdentifierCodeIsIgnored);
    addToFieldIssueMap(Field.OBSERVATION_IDENTIFIER_CODE, ObservationIdentifierCodeIsInvalid);
    addToFieldIssueMap(Field.OBSERVATION_IDENTIFIER_CODE, ObservationIdentifierCodeIsMissing);
    addToFieldIssueMap(Field.OBSERVATION_IDENTIFIER_CODE, ObservationIdentifierCodeIsUnrecognized);
    addToFieldIssueMap(Field.OBSERVATION_VALUE, ObservationValueIsMissing);
    addToFieldIssueMap(Field.OBSERVATION_DATE_TIME_OF_OBSERVATION, ObservationDateTimeOfObservationIsMissing);
    addToFieldIssueMap(Field.OBSERVATION_DATE_TIME_OF_OBSERVATION, ObservationDateTimeOfObservationIsInvalid);
    addToFieldIssueMap(Field.PATIENT_ADDRESS, PatientAddressIsMissing);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_CITY, PatientAddressCityIsInvalid);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_CITY, PatientAddressCityIsMissing);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_CITY, PatientAddressCityIsTooShort);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_CITY, PatientAddressCityIsUnexpectedlyShort);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_CITY, PatientAddressCityIsUnexpectedlyLong);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_CITY, PatientAddressCityIsTooLong);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_COUNTRY, PatientAddressCountryIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_COUNTRY, PatientAddressCountryIsIgnored);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_COUNTRY, PatientAddressCountryIsInvalid);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_COUNTRY, PatientAddressCountryIsMissing);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_COUNTRY, PatientAddressCountryIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_COUNTY, PatientAddressCountyIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_COUNTY, PatientAddressCountyIsIgnored);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_COUNTY, PatientAddressCountyIsInvalid);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_COUNTY, PatientAddressCountyIsMissing);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_COUNTY, PatientAddressCountyIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_STATE, PatientAddressStateIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_STATE, PatientAddressStateIsIgnored);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_STATE, PatientAddressStateIsInvalid);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_STATE, PatientAddressStateIsMissing);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_STATE, PatientAddressStateIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_STREET, PatientAddressStreetIsMissing);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_STREET2, PatientAddressStreet2IsMissing);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_TYPE, PatientAddressTypeIsMissing);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_TYPE, PatientAddressTypeIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_TYPE, PatientAddressTypeIsIgnored);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_TYPE, PatientAddressTypeIsInvalid);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_TYPE, PatientAddressTypeIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_TYPE, PatientAddressTypeIsValuedBadAddress);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_ZIP, PatientAddressZipIsInvalid);
    addToFieldIssueMap(Field.PATIENT_ADDRESS_ZIP, PatientAddressZipIsMissing);
    addToFieldIssueMap(Field.PATIENT_ALIAS, PatientAliasIsMissing);
    addToFieldIssueMap(Field.PATIENT_BIRTH_DATE, PatientBirthDateIsAfterSubmission);
    addToFieldIssueMap(Field.PATIENT_BIRTH_DATE, PatientBirthDateIsInFuture);
    addToFieldIssueMap(Field.PATIENT_BIRTH_DATE, PatientBirthDateIsInvalid);
    addToFieldIssueMap(Field.PATIENT_BIRTH_DATE, PatientBirthDateIsMissing);
    addToFieldIssueMap(Field.PATIENT_BIRTH_DATE, PatientBirthDateIsUnderage);
    addToFieldIssueMap(Field.PATIENT_BIRTH_DATE, PatientBirthDateIsVeryLongAgo);
    addToFieldIssueMap(Field.PATIENT_BIRTH_INDICATOR, PatientBirthIndicatorIsInvalid);
    addToFieldIssueMap(Field.PATIENT_BIRTH_INDICATOR, PatientBirthIndicatorIsMissing);
    addToFieldIssueMap(Field.PATIENT_BIRTH_ORDER, PatientBirthOrderIsInvalid);
    addToFieldIssueMap(Field.PATIENT_BIRTH_ORDER, PatientBirthOrderIsMissing);
    addToFieldIssueMap(Field.PATIENT_BIRTH_ORDER, PatientBirthOrderIsMissingAndMultipleBirthIndicated);
    addToFieldIssueMap(Field.PATIENT_BIRTH_PLACE, PatientBirthPlaceIsMissing);
    addToFieldIssueMap(Field.PATIENT_BIRTH_PLACE, PatientBirthPlaceIsTooShort);
    addToFieldIssueMap(Field.PATIENT_BIRTH_PLACE, PatientBirthPlaceIsUnexpectedlyShort);
    addToFieldIssueMap(Field.PATIENT_BIRTH_PLACE, PatientBirthPlaceIsUnexpectedlyLong);
    addToFieldIssueMap(Field.PATIENT_BIRTH_PLACE, PatientBirthPlaceIsTooLong);
    addToFieldIssueMap(Field.PATIENT_BIRTH_REGISTRY_ID, PatientBirthRegistryIdIsInvalid);
    addToFieldIssueMap(Field.PATIENT_BIRTH_REGISTRY_ID, PatientBirthRegistryIdIsMissing);
    addToFieldIssueMap(Field.PATIENT_CLASS, PatientClassIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_CLASS, PatientClassIsIgnored);
    addToFieldIssueMap(Field.PATIENT_CLASS, PatientClassIsInvalid);
    addToFieldIssueMap(Field.PATIENT_CLASS, PatientClassIsMissing);
    addToFieldIssueMap(Field.PATIENT_CLASS, PatientClassIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_DEATH_DATE, PatientDeathDateIsBeforeBirth);
    addToFieldIssueMap(Field.PATIENT_DEATH_DATE, PatientDeathDateIsInFuture);
    addToFieldIssueMap(Field.PATIENT_DEATH_DATE, PatientDeathDateIsInvalid);
    addToFieldIssueMap(Field.PATIENT_DEATH_DATE, PatientDeathDateIsMissing);
    addToFieldIssueMap(Field.PATIENT_DEATH_INDICATOR, PatientDeathIndicatorIsInconsistent);
    addToFieldIssueMap(Field.PATIENT_DEATH_INDICATOR, PatientDeathIndicatorIsMissing);
    addToFieldIssueMap(Field.PATIENT_ETHNICITY, PatientEthnicityIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_ETHNICITY, PatientEthnicityIsIgnored);
    addToFieldIssueMap(Field.PATIENT_ETHNICITY, PatientEthnicityIsInvalid);
    addToFieldIssueMap(Field.PATIENT_ETHNICITY, PatientEthnicityIsMissing);
    addToFieldIssueMap(Field.PATIENT_ETHNICITY, PatientEthnicityIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_GENDER, PatientGenderIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_GENDER, PatientGenderIsIgnored);
    addToFieldIssueMap(Field.PATIENT_GENDER, PatientGenderIsInvalid);
    addToFieldIssueMap(Field.PATIENT_GENDER, PatientGenderIsMissing);
    addToFieldIssueMap(Field.PATIENT_GENDER, PatientGenderIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_ADDRESS, PatientGuardianAddressIsMissing);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_ADDRESS_CITY, PatientGuardianAddressCityIsMissing);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_ADDRESS_STATE, PatientGuardianAddressStateIsMissing);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_ADDRESS_STREET, PatientGuardianAddressStreetIsMissing);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_ADDRESS_ZIP, PatientGuardianAddressZipIsMissing);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_NAME, PatientGuardianNameIsMissing);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_NAME, PatientGuardianNameIsSameAsUnderagePatient);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_NAME, PatientGuardianNameHasJunkName);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_NAME_FIRST, PatientGuardianNameFirstIsMissing);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_NAME_LAST, PatientGuardianNameLastIsMissing);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_RESPONSIBLE_PARTY, PatientGuardianResponsiblePartyIsMissing);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_PHONE, PatientGuardianPhoneIsMissing);
    addToFieldIssueMap(Field.PATIENT_GUARDIAN_RELATIONSHIP, PatientGuardianRelationshipIsMissing);
    addToFieldIssueMap(Field.PATIENT_IMMUNITY_CODE, PatientImmunityCodeIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_IMMUNITY_CODE, PatientImmunityCodeIsIgnored);
    addToFieldIssueMap(Field.PATIENT_IMMUNITY_CODE, PatientImmunityCodeIsInvalid);
    addToFieldIssueMap(Field.PATIENT_IMMUNITY_CODE, PatientImmunityCodeIsMissing);
    addToFieldIssueMap(Field.PATIENT_IMMUNITY_CODE, PatientImmunityCodeIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_IMMUNIZATION_REGISTRY_STATUS, PatientImmunizationRegistryStatusIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_IMMUNIZATION_REGISTRY_STATUS, PatientImmunizationRegistryStatusIsIgnored);
    addToFieldIssueMap(Field.PATIENT_IMMUNIZATION_REGISTRY_STATUS, PatientImmunizationRegistryStatusIsInvalid);
    addToFieldIssueMap(Field.PATIENT_IMMUNIZATION_REGISTRY_STATUS, PatientImmunizationRegistryStatusIsMissing);
    addToFieldIssueMap(Field.PATIENT_IMMUNIZATION_REGISTRY_STATUS, PatientImmunizationRegistryStatusIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_MEDICAID_NUMBER, PatientMedicaidNumberIsInvalid);
    addToFieldIssueMap(Field.PATIENT_MEDICAID_NUMBER, PatientMedicaidNumberIsMissing);
    addToFieldIssueMap(Field.PATIENT_MIDDLE_NAME, PatientMiddleNameIsMissing);
    addToFieldIssueMap(Field.PATIENT_MIDDLE_NAME, PatientMiddleNameIsInvalid);
    addToFieldIssueMap(Field.PATIENT_MIDDLE_NAME, PatientMiddleNameMayBeInitial);
    addToFieldIssueMap(Field.PATIENT_MIDDLE_NAME, PatientMiddleNameIsTooShort);
    addToFieldIssueMap(Field.PATIENT_MIDDLE_NAME, PatientMiddleNameIsUnexpectedlyShort);
    addToFieldIssueMap(Field.PATIENT_MIDDLE_NAME, PatientMiddleNameIsUnexpectedlyLong);
    addToFieldIssueMap(Field.PATIENT_MIDDLE_NAME, PatientMiddleNameIsTooLong);
    addToFieldIssueMap(Field.PATIENT_MOTHERS_MAIDEN_NAME, PatientMotherSMaidenNameIsInvalid);
    addToFieldIssueMap(Field.PATIENT_MOTHERS_MAIDEN_NAME, PatientMotherSMaidenNameHasJunkName);
    addToFieldIssueMap(Field.PATIENT_MOTHERS_MAIDEN_NAME, PatientMotherSMaidenNameHasInvalidPrefixes);
    addToFieldIssueMap(Field.PATIENT_MOTHERS_MAIDEN_NAME, PatientMotherSMaidenNameIsMissing);
    addToFieldIssueMap(Field.PATIENT_MOTHERS_MAIDEN_NAME, PatientMotherSMaidenNameIsTooShort);
    addToFieldIssueMap(Field.PATIENT_MOTHERS_MAIDEN_NAME, PatientMotherSMaidenNameIsUnexpectedlyShort);
    addToFieldIssueMap(Field.PATIENT_MOTHERS_MAIDEN_NAME, PatientMotherSMaidenNameIsUnexpectedlyLong);
    addToFieldIssueMap(Field.PATIENT_MOTHERS_MAIDEN_NAME, PatientMotherSMaidenNameIsTooLong);
    addToFieldIssueMap(Field.PATIENT_NAME, PatientNameMayBeTemporaryNewbornName);
    addToFieldIssueMap(Field.PATIENT_NAME, PatientNameMayBeTestName);
    addToFieldIssueMap(Field.PATIENT_NAME, PatientNameHasJunkName);
    addToFieldIssueMap(Field.PATIENT_NAME, PatientNameIsAKnownTestName);
    addToFieldIssueMap(Field.PATIENT_NAME_FIRST, PatientNameFirstIsInvalid);
    addToFieldIssueMap(Field.PATIENT_NAME_FIRST, PatientNameFirstIsMissing);
    addToFieldIssueMap(Field.PATIENT_NAME_FIRST, PatientNameFirstIsTooShort);
    addToFieldIssueMap(Field.PATIENT_NAME_FIRST, PatientNameFirstIsUnexpectedlyShort);
    addToFieldIssueMap(Field.PATIENT_NAME_FIRST, PatientNameFirstIsUnexpectedlyLong);
    addToFieldIssueMap(Field.PATIENT_NAME_FIRST, PatientNameFirstIsTooLong);
    addToFieldIssueMap(Field.PATIENT_NAME_FIRST, PatientNameFirstMayIncludeMiddleInitial);
    addToFieldIssueMap(Field.PATIENT_NAME_LAST, PatientNameLastIsInvalid);
    addToFieldIssueMap(Field.PATIENT_NAME_LAST, PatientNameLastIsMissing);
    addToFieldIssueMap(Field.PATIENT_NAME_LAST, PatientNameLastIsTooShort);
    addToFieldIssueMap(Field.PATIENT_NAME_LAST, PatientNameLastIsUnexpectedlyShort);
    addToFieldIssueMap(Field.PATIENT_NAME_LAST, PatientNameLastIsUnexpectedlyLong);
    addToFieldIssueMap(Field.PATIENT_NAME_LAST, PatientNameLastIsTooLong);
    addToFieldIssueMap(Field.PATIENT_NAME_TYPE_CODE, PatientNameTypeCodeIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_NAME_TYPE_CODE, PatientNameTypeCodeIsIgnored);
    addToFieldIssueMap(Field.PATIENT_NAME_TYPE_CODE, PatientNameTypeCodeIsInvalid);
    addToFieldIssueMap(Field.PATIENT_NAME_TYPE_CODE, PatientNameTypeCodeIsMissing);
    addToFieldIssueMap(Field.PATIENT_NAME_TYPE_CODE, PatientNameTypeCodeIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_NAME_TYPE_CODE, PatientNameTypeCodeIsNotValuedLegal);
    addToFieldIssueMap(Field.PATIENT_PHONE, PatientPhoneIsIncomplete);
    addToFieldIssueMap(Field.PATIENT_PHONE, PatientPhoneIsInvalid);
    addToFieldIssueMap(Field.PATIENT_PHONE, PatientPhoneIsMissing);
    addToFieldIssueMap(Field.PATIENT_PHONE_TEL_USE_CODE, PatientPhoneTelUseCodeIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_PHONE_TEL_USE_CODE, PatientPhoneTelUseCodeIsIgnored);
    addToFieldIssueMap(Field.PATIENT_PHONE_TEL_USE_CODE, PatientPhoneTelUseCodeIsInvalid);
    addToFieldIssueMap(Field.PATIENT_PHONE_TEL_USE_CODE, PatientPhoneTelUseCodeIsMissing);
    addToFieldIssueMap(Field.PATIENT_PHONE_TEL_USE_CODE, PatientPhoneTelUseCodeIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_PHONE_TEL_EQUIP_CODE, PatientPhoneTelEquipCodeIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_PHONE_TEL_EQUIP_CODE, PatientPhoneTelEquipCodeIsIgnored);
    addToFieldIssueMap(Field.PATIENT_PHONE_TEL_EQUIP_CODE, PatientPhoneTelEquipCodeIsInvalid);
    addToFieldIssueMap(Field.PATIENT_PHONE_TEL_EQUIP_CODE, PatientPhoneTelEquipCodeIsMissing);
    addToFieldIssueMap(Field.PATIENT_PHONE_TEL_EQUIP_CODE, PatientPhoneTelEquipCodeIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_FACILITY_ID, PatientPrimaryFacilityIdIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_FACILITY_ID, PatientPrimaryFacilityIdIsIgnored);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_FACILITY_ID, PatientPrimaryFacilityIdIsInvalid);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_FACILITY_ID, PatientPrimaryFacilityIdIsMissing);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_FACILITY_ID, PatientPrimaryFacilityIdIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_FACILITY_NAME, PatientPrimaryFacilityNameIsMissing);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_LANGUAGE, PatientPrimaryLanguageIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_LANGUAGE, PatientPrimaryLanguageIsIgnored);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_LANGUAGE, PatientPrimaryLanguageIsInvalid);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_LANGUAGE, PatientPrimaryLanguageIsMissing);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_LANGUAGE, PatientPrimaryLanguageIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_PHYSICIAN_ID, PatientPrimaryPhysicianIdIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_PHYSICIAN_ID, PatientPrimaryPhysicianIdIsIgnored);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_PHYSICIAN_ID, PatientPrimaryPhysicianIdIsInvalid);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_PHYSICIAN_ID, PatientPrimaryPhysicianIdIsMissing);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_PHYSICIAN_ID, PatientPrimaryPhysicianIdIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_PRIMARY_PHYSICIAN_NAME, PatientPrimaryPhysicianNameIsMissing);
    addToFieldIssueMap(Field.PATIENT_PROTECTION_INDICATOR, PatientProtectionIndicatorIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_PROTECTION_INDICATOR, PatientProtectionIndicatorIsIgnored);
    addToFieldIssueMap(Field.PATIENT_PROTECTION_INDICATOR, PatientProtectionIndicatorIsInvalid);
    addToFieldIssueMap(Field.PATIENT_PROTECTION_INDICATOR, PatientProtectionIndicatorIsMissing);
    addToFieldIssueMap(Field.PATIENT_PROTECTION_INDICATOR, PatientProtectionIndicatorIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_PROTECTION_INDICATOR, PatientProtectionIndicatorIsValuedAsNo);
    addToFieldIssueMap(Field.PATIENT_PROTECTION_INDICATOR, PatientProtectionIndicatorIsValuedAsYes);
    addToFieldIssueMap(Field.PATIENT_PUBLICITY_CODE, PatientPublicityCodeIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_PUBLICITY_CODE, PatientPublicityCodeIsIgnored);
    addToFieldIssueMap(Field.PATIENT_PUBLICITY_CODE, PatientPublicityCodeIsInvalid);
    addToFieldIssueMap(Field.PATIENT_PUBLICITY_CODE, PatientPublicityCodeIsMissing);
    addToFieldIssueMap(Field.PATIENT_PUBLICITY_CODE, PatientPublicityCodeIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_RACE, PatientRaceIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_RACE, PatientRaceIsIgnored);
    addToFieldIssueMap(Field.PATIENT_RACE, PatientRaceIsInvalid);
    addToFieldIssueMap(Field.PATIENT_RACE, PatientRaceIsMissing);
    addToFieldIssueMap(Field.PATIENT_RACE, PatientRaceIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_REGISTRY_ID, PatientRegistryIdIsMissing);
    addToFieldIssueMap(Field.PATIENT_REGISTRY_ID, PatientRegistryIdIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_REGISTRY_STATUS, PatientRegistryStatusIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_REGISTRY_STATUS, PatientRegistryStatusIsIgnored);
    addToFieldIssueMap(Field.PATIENT_REGISTRY_STATUS, PatientRegistryStatusIsInvalid);
    addToFieldIssueMap(Field.PATIENT_REGISTRY_STATUS, PatientRegistryStatusIsMissing);
    addToFieldIssueMap(Field.PATIENT_REGISTRY_STATUS, PatientRegistryStatusIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_SSN, PatientSsnIsInvalid);
    addToFieldIssueMap(Field.PATIENT_SSN, PatientSsnIsMissing);
    addToFieldIssueMap(Field.PATIENT_SUBMITTER_ID, PatientSubmitterIdIsMissing);
    addToFieldIssueMap(Field.PATIENT_SUBMITTER_ID_AUTHORITY, PatientSubmitterIdAuthorityIsMissing);
    addToFieldIssueMap(Field.PATIENT_SUBMITTER_ID_TYPE_CODE, PatientSubmitterIdTypeCodeIsMissing);
    addToFieldIssueMap(Field.PATIENT_SUBMITTER_ID_TYPE_CODE, PatientSubmitterIdTypeCodeIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_SUBMITTER_ID_TYPE_CODE, PatientSubmitterIdTypeCodeIsInvalid);
    addToFieldIssueMap(Field.PATIENT_SUBMITTER_ID_TYPE_CODE, PatientSubmitterIdTypeCodeIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_SUBMITTER_ID_TYPE_CODE, PatientSubmitterIdTypeCodeIsIgnored);
    addToFieldIssueMap(Field.PATIENT_SYSTEM_CREATION_DATE, PatientSystemCreationDateIsMissing);
    addToFieldIssueMap(Field.PATIENT_SYSTEM_CREATION_DATE, PatientSystemCreationDateIsInvalid);
    addToFieldIssueMap(Field.PATIENT_SYSTEM_CREATION_DATE, PatientSystemCreationDateIsBeforeBirth);
    addToFieldIssueMap(Field.PATIENT_SYSTEM_CREATION_DATE, PatientSystemCreationDateIsInFuture);
    addToFieldIssueMap(Field.PATIENT_VFC_EFFECTIVE_DATE, PatientVfcEffectiveDateIsBeforeBirth);
    addToFieldIssueMap(Field.PATIENT_VFC_EFFECTIVE_DATE, PatientVfcEffectiveDateIsInFuture);
    addToFieldIssueMap(Field.PATIENT_VFC_EFFECTIVE_DATE, PatientVfcEffectiveDateIsInvalid);
    addToFieldIssueMap(Field.PATIENT_VFC_EFFECTIVE_DATE, PatientVfcEffectiveDateIsMissing);
    addToFieldIssueMap(Field.PATIENT_VFC_STATUS, PatientVfcStatusIsDeprecated);
    addToFieldIssueMap(Field.PATIENT_VFC_STATUS, PatientVfcStatusIsIgnored);
    addToFieldIssueMap(Field.PATIENT_VFC_STATUS, PatientVfcStatusIsInvalid);
    addToFieldIssueMap(Field.PATIENT_VFC_STATUS, PatientVfcStatusIsMissing);
    addToFieldIssueMap(Field.PATIENT_VFC_STATUS, PatientVfcStatusIsUnrecognized);
    addToFieldIssueMap(Field.PATIENT_WIC_ID, PatientWicIdIsInvalid);
    addToFieldIssueMap(Field.PATIENT_WIC_ID, PatientWicIdIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ACTION_CODE, VaccinationActionCodeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_ACTION_CODE, VaccinationActionCodeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_ACTION_CODE, VaccinationActionCodeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_ACTION_CODE, VaccinationActionCodeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ACTION_CODE, VaccinationActionCodeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_ACTION_CODE, VaccinationActionCodeIsValuedAsAdd);
    addToFieldIssueMap(Field.VACCINATION_ACTION_CODE, VaccinationActionCodeIsValuedAsAddOrUpdate);
    addToFieldIssueMap(Field.VACCINATION_ACTION_CODE, VaccinationActionCodeIsValuedAsDelete);
    addToFieldIssueMap(Field.VACCINATION_ACTION_CODE, VaccinationActionCodeIsValuedAsUpdate);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeIsInvalidForDateAdministered);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeIsNotSpecific);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeIsNotVaccine);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeIsUnexpectedForDateAdministered);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeIsValuedAsNotAdministered);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeIsValuedAsUnknown);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE, VaccinationAdminCodeMayBeVariationOfPreviouslyReportedCodes);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE_TABLE, VaccinationAdminCodeTableIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_CODE_TABLE, VaccinationAdminCodeTableIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsAfterLotExpirationDate);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsAfterMessageSubmitted);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsAfterPatientDeathDate);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsAfterSystemEntryDate);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsBeforeBirth);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsBeforeOrAfterExpectedVaccineUsageRange);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsBeforeOrAfterLicensedVaccineRange);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsBeforeOrAfterWhenExpectedForPatientAge);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsBeforeOrAfterWhenValidForPatientAge);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsOn15ThDayOfMonth);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsOnFirstDayOfMonth);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsOnLastDayOfMonth);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE, VaccinationAdminDateIsReportedLate);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE_END, VaccinationAdminDateEndIsDifferentFromStartDate);
    addToFieldIssueMap(Field.VACCINATION_ADMIN_DATE_END, VaccinationAdminDateEndIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ADMINISTERED_AMOUNT, VaccinationAdministeredAmountIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_ADMINISTERED_AMOUNT, VaccinationAdministeredAmountIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ADMINISTERED_AMOUNT, VaccinationAdministeredAmountIsValuedAsZero);
    addToFieldIssueMap(Field.VACCINATION_ADMINISTERED_AMOUNT, VaccinationAdministeredAmountIsValuedAsUnknown);
    addToFieldIssueMap(Field.VACCINATION_ADMINISTERED_UNIT, VaccinationAdministeredUnitIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_ADMINISTERED_UNIT, VaccinationAdministeredUnitIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_ADMINISTERED_UNIT, VaccinationAdministeredUnitIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_ADMINISTERED_UNIT, VaccinationAdministeredUnitIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ADMINISTERED_UNIT, VaccinationAdministeredUnitIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_BODY_ROUTE, VaccinationBodyRouteIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_BODY_ROUTE, VaccinationBodyRouteIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_BODY_ROUTE, VaccinationBodyRouteIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_BODY_ROUTE, VaccinationBodyRouteIsInvalidForVaccineIndicated);
    addToFieldIssueMap(Field.VACCINATION_BODY_ROUTE, VaccinationBodyRouteIsInvalidForBodySiteIndicated);
    addToFieldIssueMap(Field.VACCINATION_BODY_ROUTE, VaccinationBodyRouteIsMissing);
    addToFieldIssueMap(Field.VACCINATION_BODY_ROUTE, VaccinationBodyRouteIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_BODY_SITE, VaccinationBodySiteIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_BODY_SITE, VaccinationBodySiteIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_BODY_SITE, VaccinationBodySiteIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_BODY_SITE, VaccinationBodySiteIsInvalidForVaccineIndicated);
    addToFieldIssueMap(Field.VACCINATION_BODY_SITE, VaccinationBodySiteIsMissing);
    addToFieldIssueMap(Field.VACCINATION_BODY_SITE, VaccinationBodySiteIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_COMPLETION_STATUS, VaccinationCompletionStatusIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_COMPLETION_STATUS, VaccinationCompletionStatusIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_COMPLETION_STATUS, VaccinationCompletionStatusIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_COMPLETION_STATUS, VaccinationCompletionStatusIsMissing);
    addToFieldIssueMap(Field.VACCINATION_COMPLETION_STATUS, VaccinationCompletionStatusIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_COMPLETION_STATUS, VaccinationCompletionStatusIsValuedAsCompleted);
    addToFieldIssueMap(Field.VACCINATION_COMPLETION_STATUS, VaccinationCompletionStatusIsValuedAsNotAdministered);
    addToFieldIssueMap(Field.VACCINATION_COMPLETION_STATUS, VaccinationCompletionStatusIsValuedAsPartiallyAdministered);
    addToFieldIssueMap(Field.VACCINATION_COMPLETION_STATUS, VaccinationCompletionStatusIsValuedAsRefused);
    addToFieldIssueMap(Field.VACCINATION_CONFIDENTIALITY_CODE, VaccinationConfidentialityCodeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_CONFIDENTIALITY_CODE, VaccinationConfidentialityCodeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_CONFIDENTIALITY_CODE, VaccinationConfidentialityCodeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_CONFIDENTIALITY_CODE, VaccinationConfidentialityCodeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_CONFIDENTIALITY_CODE, VaccinationConfidentialityCodeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_CONFIDENTIALITY_CODE, VaccinationConfidentialityCodeIsValuedAsRestricted);
    addToFieldIssueMap(Field.VACCINATION_CPT_CODE, VaccinationCptCodeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_CPT_CODE, VaccinationCptCodeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_CPT_CODE, VaccinationCptCodeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_CPT_CODE, VaccinationCptCodeIsInvalidForDateAdministered);
    addToFieldIssueMap(Field.VACCINATION_CPT_CODE, VaccinationCptCodeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_CPT_CODE, VaccinationCptCodeIsUnexpectedForDateAdministered);
    addToFieldIssueMap(Field.VACCINATION_CPT_CODE, VaccinationCptCodeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_CVX_CODE, VaccinationCvxCodeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_CVX_CODE, VaccinationCvxCodeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_CVX_CODE, VaccinationCvxCodeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_CVX_CODE, VaccinationCvxCodeIsInvalidForDateAdministered);
    addToFieldIssueMap(Field.VACCINATION_CVX_CODE, VaccinationCvxCodeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_CVX_CODE, VaccinationCvxCodeIsUnexpectedForDateAdministered);
    addToFieldIssueMap(Field.VACCINATION_CVX_CODE, VaccinationCvxCodeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_CVX_CODE_AND_CPT_CODE, VaccinationCvxCodeAndCptCodeAreInconsistent);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_ID, VaccinationFacilityIdIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_ID, VaccinationFacilityIdIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_ID, VaccinationFacilityIdIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_ID, VaccinationFacilityIdIsMissing);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_ID, VaccinationFacilityIdIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_NAME, VaccinationFacilityNameIsMissing);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_TYPE, VaccinationFacilityTypeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_TYPE, VaccinationFacilityTypeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_TYPE, VaccinationFacilityTypeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_TYPE, VaccinationFacilityTypeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_TYPE, VaccinationFacilityTypeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_TYPE, VaccinationFacilityTypeIsValuedAsPublic);
    addToFieldIssueMap(Field.VACCINATION_FACILITY_TYPE, VaccinationFacilityTypeIsValuedAsPrivate);
    addToFieldIssueMap(Field.VACCINATION_FILLER_ORDER_NUMBER, VaccinationFillerOrderNumberIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_FILLER_ORDER_NUMBER, VaccinationFillerOrderNumberIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_FILLER_ORDER_NUMBER, VaccinationFillerOrderNumberIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_FILLER_ORDER_NUMBER, VaccinationFillerOrderNumberIsMissing);
    addToFieldIssueMap(Field.VACCINATION_FILLER_ORDER_NUMBER, VaccinationFillerOrderNumberIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_FINANCIAL_ELIGIBILITY_CODE, VaccinationFinancialEligibilityCodeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_FINANCIAL_ELIGIBILITY_CODE, VaccinationFinancialEligibilityCodeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_FINANCIAL_ELIGIBILITY_CODE, VaccinationFinancialEligibilityCodeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_FINANCIAL_ELIGIBILITY_CODE, VaccinationFinancialEligibilityCodeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_FINANCIAL_ELIGIBILITY_CODE, VaccinationFinancialEligibilityCodeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_GIVEN_BY, VaccinationGivenByIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_GIVEN_BY, VaccinationGivenByIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_GIVEN_BY, VaccinationGivenByIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_GIVEN_BY, VaccinationGivenByIsMissing);
    addToFieldIssueMap(Field.VACCINATION_GIVEN_BY, VaccinationGivenByIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_ID, VaccinationIdIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ID_OF_RECEIVER, VaccinationIdOfReceiverIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ID_OF_RECEIVER, VaccinationIdOfReceiverIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_ID_OF_SENDER, VaccinationIdOfSenderIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ID_OF_SENDER, VaccinationIdOfSenderIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_INFORMATION_SOURCE, VaccinationInformationSourceIsAdministeredButAppearsToHistorical);
    addToFieldIssueMap(Field.VACCINATION_INFORMATION_SOURCE, VaccinationInformationSourceIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_INFORMATION_SOURCE, VaccinationInformationSourceIsHistoricalButAppearsToBeAdministered);
    addToFieldIssueMap(Field.VACCINATION_INFORMATION_SOURCE, VaccinationInformationSourceIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_INFORMATION_SOURCE, VaccinationInformationSourceIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_INFORMATION_SOURCE, VaccinationInformationSourceIsMissing);
    addToFieldIssueMap(Field.VACCINATION_INFORMATION_SOURCE, VaccinationInformationSourceIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_INFORMATION_SOURCE, VaccinationInformationSourceIsValuedAsAdministered);
    addToFieldIssueMap(Field.VACCINATION_INFORMATION_SOURCE, VaccinationInformationSourceIsValuedAsHistorical);
    addToFieldIssueMap(Field.VACCINATION_VIS, VaccinationVisIsMissing);
    addToFieldIssueMap(Field.VACCINATION_VIS, VaccinationVisIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_VIS, VaccinationVisIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_VIS_CVX_CODE, VaccinationVisCvxCodeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_VIS_CVX_CODE, VaccinationVisCvxCodeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_VIS_CVX_CODE, VaccinationVisCvxCodeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_VIS_CVX_CODE, VaccinationVisCvxCodeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_VIS_CVX_CODE, VaccinationVisCvxCodeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_VIS_DOCUMENT_TYPE, VaccinationVisDocumentTypeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_VIS_DOCUMENT_TYPE, VaccinationVisDocumentTypeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_VIS_DOCUMENT_TYPE, VaccinationVisDocumentTypeIsIncorrect);
    addToFieldIssueMap(Field.VACCINATION_VIS_DOCUMENT_TYPE, VaccinationVisDocumentTypeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_VIS_DOCUMENT_TYPE, VaccinationVisDocumentTypeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_VIS_DOCUMENT_TYPE, VaccinationVisDocumentTypeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_VIS_DOCUMENT_TYPE, VaccinationVisDocumentTypeIsOutOfDate);
    addToFieldIssueMap(Field.VACCINATION_VIS_PUBLISHED_DATE, VaccinationVisPublishedDateIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_VIS_PUBLISHED_DATE, VaccinationVisPublishedDateIsMissing);
    addToFieldIssueMap(Field.VACCINATION_VIS_PUBLISHED_DATE, VaccinationVisPublishedDateIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_VIS_PUBLISHED_DATE, VaccinationVisPublishedDateIsInFuture);
    addToFieldIssueMap(Field.VACCINATION_VIS_PRESENTED_DATE, VaccinationVisPresentedDateIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_VIS_PRESENTED_DATE, VaccinationVisPresentedDateIsMissing);
    addToFieldIssueMap(Field.VACCINATION_VIS_PRESENTED_DATE, VaccinationVisPresentedDateIsNotAdminDate);
    addToFieldIssueMap(Field.VACCINATION_VIS_PRESENTED_DATE, VaccinationVisPresentedDateIsBeforePublishedDate);
    addToFieldIssueMap(Field.VACCINATION_VIS_PRESENTED_DATE, VaccinationVisPresentedDateIsAfterAdminDate);
    addToFieldIssueMap(Field.VACCINATION_LOT_EXPIRATION_DATE, VaccinationLotExpirationDateIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_LOT_EXPIRATION_DATE, VaccinationLotExpirationDateIsMissing);
    addToFieldIssueMap(Field.VACCINATION_LOT_NUMBER, VaccinationLotNumberIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_LOT_NUMBER, VaccinationLotNumberIsMissing);
    addToFieldIssueMap(Field.VACCINATION_MANUFACTURER_CODE, VaccinationManufacturerCodeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_MANUFACTURER_CODE, VaccinationManufacturerCodeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_MANUFACTURER_CODE, VaccinationManufacturerCodeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_MANUFACTURER_CODE, VaccinationManufacturerCodeIsInvalidForDateAdministered);
    addToFieldIssueMap(Field.VACCINATION_MANUFACTURER_CODE, VaccinationManufacturerCodeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_MANUFACTURER_CODE, VaccinationManufacturerCodeIsUnexpectedForDateAdministered);
    addToFieldIssueMap(Field.VACCINATION_MANUFACTURER_CODE, VaccinationManufacturerCodeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_ORDER_CONTROL_CODE, VaccinationOrderControlCodeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_ORDER_CONTROL_CODE, VaccinationOrderControlCodeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_ORDER_CONTROL_CODE, VaccinationOrderControlCodeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_ORDER_CONTROL_CODE, VaccinationOrderControlCodeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ORDER_CONTROL_CODE, VaccinationOrderControlCodeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_ORDER_FACILITY_ID, VaccinationOrderFacilityIdIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_ORDER_FACILITY_ID, VaccinationOrderFacilityIdIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_ORDER_FACILITY_ID, VaccinationOrderFacilityIdIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_ORDER_FACILITY_ID, VaccinationOrderFacilityIdIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ORDER_FACILITY_ID, VaccinationOrderFacilityIdIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_ORDER_FACILITY_NAME, VaccinationOrderFacilityNameIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ORDERED_BY, VaccinationOrderedByIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_ORDERED_BY, VaccinationOrderedByIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_ORDERED_BY, VaccinationOrderedByIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_ORDERED_BY, VaccinationOrderedByIsMissing);
    addToFieldIssueMap(Field.VACCINATION_ORDERED_BY, VaccinationOrderedByIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_PLACER_ORDER_NUMBER, VaccinationPlacerOrderNumberIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_PLACER_ORDER_NUMBER, VaccinationPlacerOrderNumberIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_PLACER_ORDER_NUMBER, VaccinationPlacerOrderNumberIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_PLACER_ORDER_NUMBER, VaccinationPlacerOrderNumberIsMissing);
    addToFieldIssueMap(Field.VACCINATION_PLACER_ORDER_NUMBER, VaccinationPlacerOrderNumberIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_PRODUCT, VaccinationProductIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_PRODUCT, VaccinationProductIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_PRODUCT, VaccinationProductIsInvalidForDateAdministered);
    addToFieldIssueMap(Field.VACCINATION_PRODUCT, VaccinationProductIsMissing);
    addToFieldIssueMap(Field.VACCINATION_PRODUCT, VaccinationProductIsUnexpectedForDateAdministered);
    addToFieldIssueMap(Field.VACCINATION_PRODUCT, VaccinationProductIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_RECORDED_BY, VaccinationRecordedByIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_RECORDED_BY, VaccinationRecordedByIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_RECORDED_BY, VaccinationRecordedByIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_RECORDED_BY, VaccinationRecordedByIsMissing);
    addToFieldIssueMap(Field.VACCINATION_RECORDED_BY, VaccinationRecordedByIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_REFUSAL_REASON, VaccinationRefusalReasonConflictsCompletionStatus);
    addToFieldIssueMap(Field.VACCINATION_REFUSAL_REASON, VaccinationRefusalReasonIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_REFUSAL_REASON, VaccinationRefusalReasonIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_REFUSAL_REASON, VaccinationRefusalReasonIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_REFUSAL_REASON, VaccinationRefusalReasonIsMissing);
    addToFieldIssueMap(Field.VACCINATION_REFUSAL_REASON, VaccinationRefusalReasonIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_SYSTEM_ENTRY_TIME, VaccinationSystemEntryTimeIsInFuture);
    addToFieldIssueMap(Field.VACCINATION_SYSTEM_ENTRY_TIME, VaccinationSystemEntryTimeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_SYSTEM_ENTRY_TIME, VaccinationSystemEntryTimeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_TRADE_NAME, VaccinationTradeNameIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_TRADE_NAME, VaccinationTradeNameIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_TRADE_NAME, VaccinationTradeNameIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_TRADE_NAME, VaccinationTradeNameIsMissing);
    addToFieldIssueMap(Field.VACCINATION_TRADE_NAME, VaccinationTradeNameIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_TRADE_NAME_AND_VACCINE, VaccinationTradeNameAndVaccineAreInconsistent);
    addToFieldIssueMap(Field.VACCINATION_TRADE_NAME_AND_MANUFACTURER, VaccinationTradeNameAndManufacturerAreInconsistent);
    addToFieldIssueMap(Field.VACCINATION_VALIDITY_CODE, VaccinationValidityCodeIsInvalid);
    addToFieldIssueMap(Field.VACCINATION_VALIDITY_CODE, VaccinationValidityCodeIsDeprecated);
    addToFieldIssueMap(Field.VACCINATION_VALIDITY_CODE, VaccinationValidityCodeIsIgnored);
    addToFieldIssueMap(Field.VACCINATION_VALIDITY_CODE, VaccinationValidityCodeIsMissing);
    addToFieldIssueMap(Field.VACCINATION_VALIDITY_CODE, VaccinationValidityCodeIsUnrecognized);
    addToFieldIssueMap(Field.VACCINATION_VALIDITY_CODE, VaccinationValidityCodeIsValuedAsValid);
    addToFieldIssueMap(Field.VACCINATION_VALIDITY_CODE, VaccinationValidityCodeIsValuedAsInvalid);


    tx.commit();
    session.close();

    try
    {
      documentationTextProperties.load(this.getClass().getResourceAsStream(
          "/org/openimmunizationsoftware/dqa/validate/issues/documentationText.properties"));
    } catch (IOException ioe)
    {
      System.err.println("Unable to load documentation text properties: " + ioe.toString());
      ioe.printStackTrace();
    }
  }

  private PotentialIssue getPotentialIssue(Session session, String targetObject, String targetField, String issueType, String fieldValue)
  {
    PotentialIssue pi;
    String sql = "from PotentialIssue where targetObject = ?0 and targetField = ?1 and issueType = ?2";
    if (!fieldValue.equals(""))
    {
      sql += " and fieldValue = ?3";
    }
    Query query = session.createQuery(sql);
    query.setParameter(0, targetObject);
    query.setParameter(1, targetField);
    query.setParameter(2, issueType);
    if (!fieldValue.equals(""))
    {
      query.setParameter(3, fieldValue);
    }
    List<PotentialIssue> potentialIssues = query.list();
    if (potentialIssues.size() == 0)
    {
      throw new InitializationException("Potential issue " + targetObject + " " + targetField + " " + issueType + " " + fieldValue + " not found");
    }
    pi = potentialIssues.get(0);
    allPotentialIssues.add(pi);
    allPotentialIssuesMap.put(pi.getDisplayText(), pi);
    return pi;
  }

  public String getDocumentation(Field field, Map<PotentialIssue, PotentialIssueStatus> potentialIssueStatusMap, boolean errorsOnly)
  {
    if (fieldDocumentation.containsKey(field))
    {
      StringBuilder sb = new StringBuilder(fieldDocumentation.get(field));
      boolean foundError = false;
      if (potentialIssueStatusMap != null)
      {
        HashMap<String, PotentialIssue> potentialIssueMap = fieldIssueMaps.get(field);
        List<String> potentialIssueTypeList = new ArrayList<String>(potentialIssueMap.keySet());
        Collections.sort(potentialIssueTypeList);
        sb.append("<table>");
        sb.append("  <tr><th>Issue</th><th>HL7 Ref</th><th>Status</th><th>Description</th></tr>");
        for (String potentialIssueType : potentialIssueTypeList)
        {
          PotentialIssue issue = potentialIssueMap.get(potentialIssueType);
          PotentialIssueStatus potentialIssueStatus = potentialIssueStatusMap.get(issue);
          if (!errorsOnly || (potentialIssueStatus != null && potentialIssueStatus.getAction().equals(IssueAction.ERROR)))
          {
            foundError = true;
            sb.append("  <tr>");
            sb.append("    <td>" + issue.getDisplayText() + "</td>");
            String hl7Reference = issue.getHl7Reference();
            if (hl7Reference == null || hl7Reference.equals(""))
            {
              sb.append("    <td>-</td>");
            } else
            {
              sb.append("    <td>" + hl7Reference + "</td>");
            }
            if (potentialIssueStatus != null)
            {
              sb.append("    <td>" + potentialIssueStatus.getAction().getActionLabel() + "</td>");
            } else
            {
              sb.append("    <td>-</td>");
            }
            String description = documentationTextProperties.getProperty(issue.getDisplayText());
            if (description == null)
            {

              if (issue.getFieldValue() != null && !issue.getFieldValue().equals(""))
              {
                description = documentationTextProperties.getProperty(issue.getIssueType() + " " + issue.getFieldValue());
              } else
              {
                description = documentationTextProperties.getProperty(issue.getIssueType());
              }
              if (description == null)
              {
                description = "#### NOT FOUND #### USE " + issue.getIssueType() + "=";
              }
            }
            sb.append("    <td>" + description + "</td>");
            sb.append("  </tr>");
          }
        }
        sb.append("</table>");
      }
      if (errorsOnly && !foundError)
      {
        return "";
      }
      return sb.toString();
    }
    return "";
  }

  
  public String getDocumentationForAnalysis(Field field, Map<PotentialIssue, PotentialIssueStatus> potentialIssueStatusMap,
      Map<PotentialIssue, MessageReceived> potentialIssueFoundMessageReceivedExample)
  {
    if (fieldDocumentation.containsKey(field))
    {
      StringBuilder sb = null;
      if (potentialIssueStatusMap != null)
      {
        List<MessageReceived> messageReceivedExampleList = new ArrayList<MessageReceived>();
        HashMap<String, PotentialIssue> potentialIssueMap = fieldIssueMaps.get(field);
        List<String> potentialIssueTypeList = new ArrayList<String>(potentialIssueMap.keySet());
        Collections.sort(potentialIssueTypeList);
        for (String potentialIssueType : potentialIssueTypeList)
        {
          PotentialIssue issue = potentialIssueMap.get(potentialIssueType);
          PotentialIssueStatus potentialIssueStatus = potentialIssueStatusMap.get(issue);
          if (potentialIssueStatus != null && !potentialIssueStatus.getAction().equals(IssueAction.ACCEPT))
          {
            if (sb == null)
            {
              sb = new StringBuilder(fieldDocumentation.get(field));
              sb.append("<table width=\"720\">");
              sb.append("  <tr><th>Issue</th><th>HL7 Ref</th><th>Status</th><th>Description</th></tr>");
            }
            MessageReceived messageReceived = potentialIssueFoundMessageReceivedExample == null ? null : potentialIssueFoundMessageReceivedExample
                .get(issue);
            if (messageReceived != null)
            {
            	//System.out.println("IS_USEDDDDDDDDDDDDDDDDDDDDDDD");
              boolean alreadyAdded = false;
              for (MessageReceived messageReceivedAlreadySelected : messageReceivedExampleList)
              {
                if (messageReceivedAlreadySelected == messageReceived)
                {
                  alreadyAdded = true;
                  break;
                }
              }
              if (!alreadyAdded)
              {
                messageReceivedExampleList.add(messageReceived);
              }
            }
            sb.append("  <tr>");
            sb.append("    <td>" + issue.getDisplayText() + "</td>");
            String hl7Reference = issue.getHl7Reference();
            if (hl7Reference == null || hl7Reference.equals(""))
            {
              sb.append("    <td>-</td>");
            } else
            {
              sb.append("    <td>" + hl7Reference + "</td>");
            }
            if (potentialIssueStatus != null)
            {
              sb.append("    <td>" + potentialIssueStatus.getAction().getActionLabel() + "</td>");
            }
            String description = documentationTextProperties.getProperty(issue.getDisplayText());
            if (description == null)
            {
              if (issue.getFieldValue() != null && !issue.getFieldValue().equals(""))
              {
                description = documentationTextProperties.getProperty(issue.getIssueType() + " " + issue.getFieldValue());
              } else
              {
                description = documentationTextProperties.getProperty(issue.getIssueType());
              }
              if (description == null)
              {
                description = "#### NOT FOUND #### USE " + issue.getIssueType() + "=";
              }
            }
            sb.append("    <td>" + description + "</td>");
            sb.append("  </tr>");
          }
        }
        if (sb != null)
        {
          sb.append("</table>");
          if (messageReceivedExampleList.size() > 0)
          {
            for (MessageReceived messageReceived : messageReceivedExampleList)
            {
              sb.append("<h3>Example Message</h3>");
              sb.append("<pre>");
              sb.append(messageReceived.getRequestText());
              sb.append("</pre>");
              if (messageReceived.getInternalTemporaryId() > 0)
              {
                String messageName = "Message " + messageReceived.getInternalTemporaryId() + " "
                    + messageReceived.getIssueAction().getActionLabelForMessageReceivedPastTense() + "";
                sb.append("<p><a class=\"tooltip\" href=\"" + messageName + ".html\">More Information</a></p>");
              }
            }
          }
        }
      }
      if (sb == null)
      {
        return "";
      }
      return sb.toString();
    }
    return "";
  }

}
