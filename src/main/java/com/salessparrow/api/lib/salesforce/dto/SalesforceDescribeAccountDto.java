package com.salessparrow.api.lib.salesforce.dto;

import com.salessparrow.api.dto.entities.DescribeAccountFieldEntity;

import lombok.Data;

/**
 * DTO for the response of the Salesforce API when describing account.
 */
@Data
public class SalesforceDescribeAccountDto {

	private String label;

	private String name;

	private Integer length;

	private String type;

	private String defaultValue;

	private Boolean nillable;

	private Boolean createable;

	private Integer precision;

	private Integer scale;

	private PicklistValues[] picklistValues;

	@Data
	public static class PicklistValues {

		private String label;

		private String value;

		private Boolean active;

	}

	public DescribeAccountFieldEntity describeAccountFieldEntity() {

		DescribeAccountFieldEntity entity = new DescribeAccountFieldEntity();

		entity.setLabel(this.label);
		entity.setName(this.name);
		entity.setLength(this.length);
		entity.setType(this.type);
		entity.setDefaultValue(this.defaultValue);
		entity.setPrecision(this.precision);
		entity.setScale(this.scale);

		if (this.picklistValues != null) {
			DescribeAccountFieldEntity.PicklistValues[] picklistValues = new DescribeAccountFieldEntity.PicklistValues[this.picklistValues.length];

			for (int i = 0; i < this.picklistValues.length; i++) {
				DescribeAccountFieldEntity.PicklistValues entityPicklistValue = new DescribeAccountFieldEntity.PicklistValues();

				entityPicklistValue.setLabel(this.picklistValues[i].label);
				entityPicklistValue.setValue(this.picklistValues[i].value);
				entityPicklistValue.setActive(this.picklistValues[i].active);

				picklistValues[i] = entityPicklistValue;
			}

			entity.setPicklistValues(picklistValues);
		}

		return entity;
	}

}
