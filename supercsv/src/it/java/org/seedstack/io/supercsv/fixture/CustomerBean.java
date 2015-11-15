/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.io.supercsv.fixture;

import java.util.Date;

/**
 * POJO for test.
 *
 */
public class CustomerBean {

	private String customerNo;

	private String firstName;

	private String lastName;

	private Date birthDate;

	private String mailingAddress;

	private Boolean married;

	private Integer numberOfKids;

	private String favouriteQuote;

	private String email;

	private Long loyaltyPoints;

	/**
	 * Constructor.
	 */
	public CustomerBean() {
	}

	/**
	 * Constructor.
	 * 
	 * @param customerNo
	 * @param firstName
	 * @param lastName
	 * @param birthDate
	 * @param maillingAddress
	 * @param married
	 * @param numberOfKids
	 * @param favouriteQuote
	 * @param email
	 * @param loyaltyPoints
	 */
	public CustomerBean(String customerNo, String firstName, String lastName, Date birthDate, String maillingAddress,
			Boolean married, Integer numberOfKids, String favouriteQuote, String email, Long loyaltyPoints) {
		super();
		this.customerNo = customerNo;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.mailingAddress = maillingAddress;
		this.married = married;
		this.numberOfKids = numberOfKids;
		this.favouriteQuote = favouriteQuote;
		this.email = email;
		this.loyaltyPoints = loyaltyPoints;
	}
	

	/**
	 * @return the customerNo
	 */
	public String getCustomerNo() {
		return customerNo;
	}

	/**
	 * @param customerNo
	 *            the customerNo to set
	 */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the mailingAddress
	 */
	public String getMailingAddress() {
		return mailingAddress;
	}

	/**
	 * @param mailingAddress
	 *            the maillingAddress to set
	 */
	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	/**
	 * @return the married
	 */
	public Boolean getMarried() {
		return married;
	}

	/**
	 * @param married
	 *            the married to set
	 */
	public void setMarried(Boolean married) {
		this.married = married;
	}

	/**
	 * @return the numberOfKids
	 */
	public Integer getNumberOfKids() {
		return numberOfKids;
	}

	/**
	 * @param numberOfKids
	 *            the numberOfKids to set
	 */
	public void setNumberOfKids(Integer numberOfKids) {
		this.numberOfKids = numberOfKids;
	}

	/**
	 * @return the favouriteQuote
	 */
	public String getFavouriteQuote() {
		return favouriteQuote;
	}

	/**
	 * @param favouriteQuote
	 *            the favouriteQuote to set
	 */
	public void setFavouriteQuote(String favouriteQuote) {
		this.favouriteQuote = favouriteQuote;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the loyaltyPoints
	 */
	public long getLoyaltyPoints() {
		return loyaltyPoints;
	}

	/**
	 * @param loyaltyPoints
	 *            the loyaltyPoints to set
	 */
	public void setLoyaltyPoints(long loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + ((customerNo == null) ? 0 : customerNo.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((favouriteQuote == null) ? 0 : favouriteQuote.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((loyaltyPoints == null) ? 0 : loyaltyPoints.hashCode());
		result = prime * result + ((mailingAddress == null) ? 0 : mailingAddress.hashCode());
		result = prime * result + ((married == null) ? 0 : married.hashCode());
		result = prime * result + ((numberOfKids == null) ? 0 : numberOfKids.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerBean other = (CustomerBean) obj;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (customerNo == null) {
			if (other.customerNo != null)
				return false;
		} else if (!customerNo.equals(other.customerNo))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (favouriteQuote == null) {
			if (other.favouriteQuote != null)
				return false;
		} else if (!favouriteQuote.equals(other.favouriteQuote))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (loyaltyPoints == null) {
			if (other.loyaltyPoints != null)
				return false;
		} else if (!loyaltyPoints.equals(other.loyaltyPoints))
			return false;
		if (mailingAddress == null) {
			if (other.mailingAddress != null)
				return false;
		} else if (!mailingAddress.equals(other.mailingAddress))
			return false;
		if (married == null) {
			if (other.married != null)
				return false;
		} else if (!married.equals(other.married))
			return false;
		if (numberOfKids == null) {
			if (other.numberOfKids != null)
				return false;
		} else if (!numberOfKids.equals(other.numberOfKids))
			return false;
		return true;
	}

}
