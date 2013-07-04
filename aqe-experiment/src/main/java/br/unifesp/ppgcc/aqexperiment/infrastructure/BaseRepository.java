package br.unifesp.ppgcc.aqexperiment.infrastructure;

import java.lang.reflect.ParameterizedType; 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Required;

public abstract class BaseRepository<T> {

	private final Class<T> persistentClass;
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public BaseRepository() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public BaseRepository(final Class<T> persistentClass) {
		super();
		this.persistentClass = persistentClass;
	}

	public int countAll() {
		return countByCriteria();
	}

	public int countByExample(final T exampleInstance) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		crit.setProjection(Projections.rowCount());
		crit.add(Example.create(exampleInstance));

		return (Integer) crit.list().get(0);
	}

	public List<T> findAll() {
		return findByCriteria();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(final T exampleInstance) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		final List<T> result = crit.list();
		return result;
	}

	public T findById(final Long id) {
		final T result = getEntityManager().find(persistentClass, id);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNamedQuery(final String name, Object... params) {
		javax.persistence.Query query = getEntityManager().createNamedQuery(name);

		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}

		final List<T> result = (List<T>) query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNamedQueryAndNamedParams(final String name, final Map<String, ? extends Object> params) {
		javax.persistence.Query query = getEntityManager().createNamedQuery(name);

		for (final Map.Entry<String, ? extends Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}

		final List<T> result = (List<T>) query.getResultList();
		return result;
	}

	public Class<T> getEntityClass() {
		return persistentClass;
	}

	@Required
	@PersistenceContext
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	protected List<T> findByCriteria(List<Criterion> criterions) {
		return findByCriteria(-1, -1, criterions, null, null);
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	protected List<T> findByCriteria(final Criterion... criterion) {
		return findByCriteria(-1, -1, toCriterions(criterion), null, null);
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	protected List<T> findByCriteria(final Order order, final Criterion... criterion) {
		List<Order> orders = new ArrayList<Order>();
		orders.add(order);
		return findByCriteria(-1, -1, toCriterions(criterion), null, orders);
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	protected List<T> findByCriteria(final List<Order> orders, final Criterion... criterion) {
		return findByCriteria(-1, -1, toCriterions(criterion), null, orders);
	}

	private List<Criterion> toCriterions(final Criterion... criterion){
		List<Criterion> criterions = new ArrayList<Criterion>();
		for (final Criterion c : criterion) {
			criterions.add(c);
		}
		return criterions;
	}
	
	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(final int firstResult, final int maxResults, final List<Criterion> criterions, final Map<String,String> alias, final List<Order> orders) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());

		for (final Criterion c : criterions) {
			crit.add(c);
		}

		if(alias != null)
			for (final String key : alias.keySet()) {
				crit.createAlias(key, alias.get(key));
			}

		if(orders != null)
			for (final Order o : orders) {
				crit.addOrder(o);
			}

		if (firstResult > 0) {
			crit.setFirstResult(firstResult);
		}

		if (maxResults > 0) {
			crit.setMaxResults(maxResults);
		}

		final List<T> result = crit.list();
		return result;
	}

	protected int countByCriteria(Criterion... criterion) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		crit.setProjection(Projections.rowCount());

		for (final Criterion c : criterion) {
			crit.add(c);
		}

		return (Integer) crit.list().get(0);
	}

	public void delete(T entity) {
		getEntityManager().remove(entity);
	}

	public void persist(T entity) {
		getEntityManager().persist(entity);
	}

	public T save(T entity) {
		final T savedEntity = getEntityManager().merge(entity);
		return savedEntity;
	}
}