package edu.fdu.se.bean;

public class AndroidCacheCommit {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column android_cache_commit.id
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column android_cache_commit.key_entry
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	private String keyEntry;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column android_cache_commit.commit_id
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	private String commitId;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public AndroidCacheCommit(Integer id, String keyEntry, String commitId) {
		this.id = id;
		this.keyEntry = keyEntry;
		this.commitId = commitId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table android_cache_commit
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public AndroidCacheCommit() {
		super();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column android_cache_commit.id
	 * @return  the value of android_cache_commit.id
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column android_cache_commit.id
	 * @param id  the value for android_cache_commit.id
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column android_cache_commit.key_entry
	 * @return  the value of android_cache_commit.key_entry
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public String getKeyEntry() {
		return keyEntry;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column android_cache_commit.key_entry
	 * @param keyEntry  the value for android_cache_commit.key_entry
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public void setKeyEntry(String keyEntry) {
		this.keyEntry = keyEntry == null ? null : keyEntry.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column android_cache_commit.commit_id
	 * @return  the value of android_cache_commit.commit_id
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public String getCommitId() {
		return commitId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column android_cache_commit.commit_id
	 * @param commitId  the value for android_cache_commit.commit_id
	 * @mbg.generated  Sat Dec 30 19:49:26 CST 2017
	 */
	public void setCommitId(String commitId) {
		this.commitId = commitId == null ? null : commitId.trim();
	}
}