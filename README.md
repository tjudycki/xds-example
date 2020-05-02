# IPF IHE XDS SpringBoot Example

This is a first version of tutorial that shows how to use IPF to support IHE XDS and PIX, using SpringBoot.

## How to use it

1. Clone or download this project
2. Run ``startup.sh`` or ``startup.bat`` file
3. Open http://localhost:9091/services/ 

You need Java 1.8 with this version of IPF.

Change port in src/main/resources/application.properties 


## How to test it

Download client for testing https://www.soapui.org/downloads/soapui.html

### Testing ITI18:

Run SoapUI, File -> New SOAP Project 
- Project Name: XDS Example
- Initial WSDL: http://localhost:9091/services/xds-iti18?wsdl

Then:
DocumentRegistry_Binding_Soap12
+ DocumentRegistry_RegistryStoredQuery
  + Request1

Copy content of request_query.xml (006_request_query_dziala) into left panel and submit (Alt-Enter)

### Testing ITI41:

From main menu: Project - Add WSDL
- WSDL Location: http://localhost:9091/services/xds-iti41?wsdl

Copy content of 007_request_provide_register into left panel and submit.

## Problems:
- Can't create an ITI18 answer that would pass iti18ResponseValidator(). Try uncommenting this validation in XdsRouteBuilder and modify Iti18Processor to prepare proper answer.
- Look at Iti18Processor - is it proper way of returning dokuments? Shall we use MTOM instead of base64 encoding? How to change it?
- Can't prepare proper request in SoapUI for Provide'n'register DocumentSet that would containt dokument.  
