package org.guce.siat.web.gr.util;

import java.util.List;

import org.guce.siat.common.model.AuditEntity;


/**
 * The Class HistoryModel.
 */
public class HistoryModel
{

	/** The history model name. */
	private Long idModel;

	/** The list audit entity. */
	private List<AuditEntity> listAuditEntity;

	/**
	 * Instantiates a new history model.
	 */
	public HistoryModel()
	{
		super();
	}

	/**
	 * Gets the id model.
	 *
	 * @return the idModel
	 */
	public Long getIdModel()
	{
		return idModel;
	}

	/**
	 * Sets the id model.
	 *
	 * @param idModel
	 *           the idModel to set
	 */
	public void setIdModel(final Long idModel)
	{
		this.idModel = idModel;
	}

	/**
	 * Gets the list audit entity.
	 *
	 * @return the listAuditEntity
	 */
	public List<AuditEntity> getListAuditEntity()
	{
		return listAuditEntity;
	}

	/**
	 * Sets the list audit entity.
	 *
	 * @param listAuditEntity
	 *           the listAuditEntity to set
	 */
	public void setListAuditEntity(final List<AuditEntity> listAuditEntity)
	{
		this.listAuditEntity = listAuditEntity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "HistoryModel [idModel=" + idModel + ", listAuditEntity=" + listAuditEntity + "]";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int hash = 0;
		hash += (idModel != null ? idModel.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object)
	{
		if (!(object instanceof HistoryModel))
		{
			return false;
		}
		final HistoryModel other = (HistoryModel) object;
		if ((this.getIdModel() == null && other.getIdModel() != null)
				|| (this.getIdModel() != null && !this.getIdModel().equals(other.getIdModel())))
		{
			return false;
		}
		return true;
	}

}
