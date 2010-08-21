package kiwi.service.reasoning.util;

import java.util.Collections;

import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.QueryTranslator;
import org.hibernate.hql.QueryTranslatorFactory;
import org.hibernate.hql.ast.ASTQueryTranslatorFactory;

/** Translates HQL queries to SQL.
 * 
 * Credit: http://narcanti.keyboardsamurais.de/hibernate-hql-to-sql-translation.html
 * 
 * To get the sessionFactory on JBoss do:
 * 
 * org.hibernate.Session session = (Session)entityManager.getDelegate();
 * session.getSessionFactory()
 * 
 */
public class HqlToSqlTranslator {
	private static SessionFactory sessionFactory;

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HqlToSqlTranslator.sessionFactory = sessionFactory;
	}

	public static String toSql(String hqlQueryText) {
		if (hqlQueryText != null && hqlQueryText.trim().length() > 0) {
			final QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
			final SessionFactoryImplementor factory = (SessionFactoryImplementor) sessionFactory;
			final QueryTranslator translator = translatorFactory
					.createQueryTranslator(hqlQueryText, hqlQueryText,
							Collections.EMPTY_MAP, factory);
			translator.compile(Collections.EMPTY_MAP, false);
			return translator.getSQLString();
		}
		return null;
	}
}


