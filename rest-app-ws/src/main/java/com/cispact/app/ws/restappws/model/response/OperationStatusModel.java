package com.cispact.app.ws.restappws.model.response;

//this class will return opeartion name and opearation sucess nor not
public class OperationStatusModel {

	private String operationResult;

	private String operationName;

	public OperationStatusModel() {
		super();
	}

	public String getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(String operationResult) {
		this.operationResult = operationResult;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}



}
