/**
 * 
 */
package com.vincent.coretest.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;

public class CriteriaUtil {
	public final static int MAX_SQL_IN_SIZE = 900;

	public static Predicate[] genMultiInPredicateArr(CriteriaBuilder cb, Root<?> from, List<?> inTargets,
			String attrName) {
		List<Predicate> predicateList = new LinkedList<>();
		int size = inTargets.size();
		for (int i = 0; i < size; i += MAX_SQL_IN_SIZE) {
			int toIndex = i + MAX_SQL_IN_SIZE > size ? size : i + MAX_SQL_IN_SIZE;
			predicateList.add(cb.in(from.get(attrName)).value(inTargets.subList(i, toIndex)));
		}
		return predicateList.toArray(new Predicate[predicateList.size()]);
	}

	@SuppressWarnings("rawtypes")
	public static Specification genMultiInSpec(List<?> inTargets, String attrName) {
		PredicateBuilder pb = Specifications.or();
		int size = inTargets.size();
		for (int i = 0; i < size; i += MAX_SQL_IN_SIZE) {
			int toIndex = i + MAX_SQL_IN_SIZE > size ? size : i + MAX_SQL_IN_SIZE;
			pb = pb.in(attrName, inTargets.subList(i, toIndex).toArray());
		}
		return pb.build();
	}

	@SuppressWarnings("rawtypes")
	public static Specification genAndInSpec(List<?> inTargets, String attrName) {
		PredicateBuilder pb = Specifications.and();
		pb = pb.in(attrName, inTargets.toArray());
		return pb.build();
	}

	@SuppressWarnings("rawtypes")
	public static Specification genAndNotInSpec(List<?> inTargets, String attrName) {
		PredicateBuilder pb = Specifications.and();
		pb = pb.notIn(attrName, inTargets.toArray());
		return pb.build();
	}

	@SuppressWarnings("rawtypes")
	public static Specification genIsValueSpec(Object value, String attrName) {
		PredicateBuilder pb = Specifications.and();
		pb = pb.eq(attrName, value);
		return pb.build();
	}

	@SuppressWarnings("rawtypes")
	public static Specification genIsNullSpec(String attrName) {
		PredicateBuilder pb = Specifications.and();
		pb = pb.eq(attrName, (Object) null);
		return pb.build();
	}

	@SuppressWarnings("rawtypes")
	public static Specification genIsNotNullSpec(String attrName) {
		PredicateBuilder pb = Specifications.and();
		pb = pb.ne(attrName, (Object) null);
		return pb.build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Specification genTimeGreaterThanSpec(Date value, String attrName) {
		PredicateBuilder pb = Specifications.and();
		pb = pb.gt(attrName, value);
		return pb.build();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Specification genTimeGreaterThanEqualSpec(Date value, String attrName) {
		PredicateBuilder pb = Specifications.and();
		pb = pb.gt(attrName, value);
		return pb.build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Specification genTimeLessThanSpec(Date value, String attrName) {
		PredicateBuilder pb = Specifications.and();
		pb = pb.le(attrName, value);
		return pb.build();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Specification genTimeLessThanEqualSpec(Date value, String attrName) {
		PredicateBuilder pb = Specifications.and();
		pb = pb.le(attrName, value);
		return pb.build();
	}
}
