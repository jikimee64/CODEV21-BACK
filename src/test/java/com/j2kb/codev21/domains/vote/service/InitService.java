package com.j2kb.codev21.domains.vote.service;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.j2kb.codev21.domains.board.domain.Board;
import com.j2kb.codev21.domains.user.domain.Field;
import com.j2kb.codev21.domains.user.domain.Status;
import com.j2kb.codev21.domains.user.domain.User;

@Profile("vote_test_init")
@Component
class InitService {

	@Autowired 
	private InitDb initDb;
	
	@PostConstruct
	public void init() {
		initDb.init();
	}
	
	@Component
	@Transactional
	static class InitDb {
		
		@Autowired
		private EntityManager em;
		
		@Transactional
		public void init() {
			for (int i = 1; i <= 10; i++) {
				em.persist(new User("email" + i, "password" + i, "name" + i, "joinGisu" + i, Status.ACTIVE, Field.NONE,
						"githubId" + i));
			}
			em.flush();
			List<User> resultList = em.createQuery("select u from User u", User.class).getResultList();
			Iterator<User> iterator = resultList.iterator();
			for (int i = 1; i <= 10; i++) {
				em.persist(new Board("title" + i, "content" + i, "summary" + i, "writer" + i, "image" + i,
						iterator.next()));
			}
			em.flush();
			em.clear();
		}
	}
}

