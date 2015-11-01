package com.tapwisdom.core.daos;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.EntityDao;
import com.tapwisdom.core.daos.documents.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by srividyak on 03/08/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class EntityDaoTest {
    
    @Autowired
    private EntityDao entityDao;
    
    @Autowired
    private MongoOperations operations;
    
    private static final int userUpVoteCount = 100,
                            userTapCount = 10,
                            userViewCount = 1000,
                            qnaUpVoteCount = 100,
                            qnaTapCount = 10,
                            qnaViewCount = 100,
                            companyViewCount = 1000,
                            companyUpVoteCount = 100,
                            companyTapCount = 1;
    
    private void createUserEntity() {
        EntityCharacteristics<User> userEntity = new EntityCharacteristics<User>();
        User targetUser = new User();
        targetUser.setId("targetUser");
        targetUser.setEmail("target@email.com");
        userEntity.setEntity(targetUser);
        userEntity.setType(EntityType.USER);
        userEntity.setUpVoteCount(userUpVoteCount);
        userEntity.setTapCount(userTapCount);
        userEntity.setViewCount(userViewCount);
        try {
            entityDao.save(userEntity);
            UserEntityRelation<User> userEntityRelation = new UserEntityRelation<User>();
            userEntityRelation.setEntity(targetUser);
            userEntityRelation.setUserId("myId");
            UserEntityRelation.EntityView entityView = new UserEntityRelation.EntityView();
            entityView.setViewCount(userViewCount);
            userEntityRelation.setEntityView(entityView);
            userEntityRelation.setType(EntityType.USER);
            entityDao.save(userEntityRelation);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    private void createQnAEntity() {
        EntityCharacteristics<QnAEntity> qnAEntity = new EntityCharacteristics<QnAEntity>();
        QnAEntity qnA = new QnAEntity();
        qnA.setQuestion(new Question());
        qnA.setAnswer("");
        qnA.setId("qnaId");
        qnAEntity.setEntity(qnA);
        qnAEntity.setType(EntityType.QNA);
        qnAEntity.setTapCount(qnaTapCount);
        qnAEntity.setUpVoteCount(qnaUpVoteCount);
        qnAEntity.setViewCount(qnaViewCount);
        try {
            entityDao.save(qnAEntity);
            UserEntityRelation<QnAEntity> userEntityRelation = new UserEntityRelation<QnAEntity>();
            userEntityRelation.setEntity(qnA);
            UserEntityRelation.EntityTap entityTap = new UserEntityRelation.EntityTap();
            entityTap.setFolderName("myFolder");
            entityTap.setTap(true);
            userEntityRelation.setEntityTap(entityTap);
            userEntityRelation.setUserId("myId");
            userEntityRelation.setType(EntityType.QNA);
            entityDao.save(userEntityRelation);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    private void createCompanyEntity() {
        EntityCharacteristics<Company> companyEntity = new EntityCharacteristics<Company>();
        Company company = new Company();
        company.setName("tapwisdom");
        company.setIndustry("software");
        company.setId("companyId");
        companyEntity.setEntity(company);
        companyEntity.setType(EntityType.COMPANY);
        companyEntity.setUpVoteCount(companyUpVoteCount);
        companyEntity.setTapCount(companyTapCount);
        companyEntity.setViewCount(companyViewCount);
        try {
            entityDao.save(companyEntity);
            UserEntityRelation<Company> userEntityRelation = new UserEntityRelation<Company>();
            userEntityRelation.setEntity(company);
            userEntityRelation.setUserId("myId");
            UserEntityRelation.EntityVote entityVote = new UserEntityRelation.EntityVote();
            entityVote.setUpVote(true);
            entityVote.setTimestamp(new Date().getTime());
            userEntityRelation.setEntityVote(entityVote);
            userEntityRelation.setType(EntityType.COMPANY);
            entityDao.save(userEntityRelation);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Before
    public void createEntities() {
        createUserEntity();
        createQnAEntity();
        createCompanyEntity();
    }
    
    @Test
    public void getUserEntityByEntityIdAndUserTest() {
        EntityViewable<User> entityViewable = entityDao.getEntityInfoById("targetUser", EntityType.USER, "myId");
        assert entityViewable != null;
        assert entityViewable.getEntityView() != null;
        assert entityViewable.getEntityView().getViewCount() > 0;
        assert entityViewable.getEntityCharacteristics() != null;
        assert entityViewable.getEntityCharacteristics().getType() == EntityType.USER;
    }
    
    @Test
    public void getUserEntityByEntityIdTest() {
        EntityCharacteristics<User> entityCharacteristics = entityDao.getEntityInfoById("targetUser", EntityType.USER);
        assert entityCharacteristics != null;
        assert entityCharacteristics.getEntity() != null;
        assert entityCharacteristics.getType() == EntityType.USER;
        assert entityCharacteristics.getViewCount() > 0;
        assert entityCharacteristics.getEntity().getId().equals("targetUser");
    }
    
    @Test
    public void getCompanyEntityByEntityIdAndUserTest() {
        EntityViewable<Company> entityViewable = entityDao.getEntityInfoById("companyId", EntityType.COMPANY, "myId");
        assert entityViewable != null;
        assert entityViewable.getEntityVote() != null;
        assert entityViewable.getEntityVote().getUpVote();
        assert entityViewable.getEntityCharacteristics() != null;
        assert entityViewable.getEntityCharacteristics().getType() == EntityType.COMPANY;
    }

    @Test
    public void getCompanyEntityByEntityIdTest() {
        EntityCharacteristics<Company> company = entityDao.getEntityInfoById("companyId", EntityType.COMPANY);
        assert company != null;
        assert company.getEntity() != null;
        assert company.getEntity().getId().equals("companyId");
        assert company.getUpVoteCount() > 0;
        assert company.getType() == EntityType.COMPANY;
    }
    
    @Test
    public void getQnAEntityByEntityIdAndUserTest() {
        EntityViewable<QnAEntity> entityViewable = entityDao.getEntityInfoById("qnaId", EntityType.QNA, "myId");
        assert entityViewable != null;
        assert entityViewable.getEntityTap() != null;
        assert entityViewable.getEntityTap().getTap();
        assert entityViewable.getEntityCharacteristics() != null;
        assert entityViewable.getEntityCharacteristics().getType() == EntityType.QNA;
    }

    @Test
    public void getQnAEntityByEntityIdTest() {
        EntityCharacteristics<QnAEntity> qnAEntity = entityDao.getEntityInfoById("qnaId", EntityType.QNA);
        assert qnAEntity != null;
        assert qnAEntity.getEntity() != null;
        assert qnAEntity.getEntity().getId().equals("qnaId");
        assert qnAEntity.getTapCount() > 0;
        assert qnAEntity.getType() == EntityType.QNA;
    }
    
    @Test
    public void tapQnATest() {
        QnAEntity qnAEntity = new QnAEntity();
        qnAEntity.setId("qnaId");
        try {
            entityDao.tapEntity(qnAEntity, EntityType.QNA, "newUser", "folder");
            EntityViewable<QnAEntity> entityViewable = entityDao.getEntityInfoById("qnaId", EntityType.QNA, "newUser");
            EntityCharacteristics<QnAEntity> qnAEntityEntityCharacteristics = entityDao.getEntityInfoById("qnaId", EntityType.QNA);
            assert qnAEntityEntityCharacteristics != null;
            assert qnAEntityEntityCharacteristics.getTapCount() == qnaTapCount + 1;
            assert entityViewable != null;
            assert entityViewable.getEntityTap() != null;
            assert entityViewable.getEntityTap().getTap() == true;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void unTapQnATest() {
        QnAEntity qnAEntity = new QnAEntity();
        qnAEntity.setId("qnaId");
        entityDao.unTapEntity(qnAEntity, EntityType.QNA, "newUser", "folder");
        EntityViewable<QnAEntity> entityViewable = entityDao.getEntityInfoById("qnaId", EntityType.QNA, "newUser");
        EntityCharacteristics<QnAEntity> qnAEntityEntityCharacteristics = entityDao.getEntityInfoById("qnaId", EntityType.QNA);
        assert qnAEntityEntityCharacteristics != null;
        assert qnAEntityEntityCharacteristics.getTapCount() == qnaTapCount - 1;
        assert entityViewable != null;
        assert entityViewable.getEntityTap() != null;
        assert entityViewable.getEntityTap().getTap() == false;
    }
    
    @Test
    public void viewEntityTest() {
        User user = new User();
        user.setId("targetUser");
        entityDao.viewEntity(user, EntityType.USER, "newUser");
        EntityViewable<UberEntity> entityViewable = entityDao.getEntityInfoById(user.getId(), EntityType.USER, "newUser");
        EntityCharacteristics<UberEntity> entityEntityCharacteristics = entityDao.getEntityInfoById(user.getId(), EntityType.USER);
        assert entityViewable != null;
        assert entityViewable.getEntityView() != null;
        assert entityEntityCharacteristics != null;
        assert entityEntityCharacteristics.getEntity().getId().equals(user.getId());
    }
    
    @Test
    public void upVoteEntityTest() {
        Company company = new Company();
        company.setId("companyId");
        company.setIndustry("software");
        entityDao.upVoteEntity(company, EntityType.COMPANY, "newUser");
        EntityViewable<Company> entityViewable = entityDao.getEntityInfoById(company.getId(), EntityType.COMPANY, "newUser");
        EntityCharacteristics<Company> entityCharacteristics = entityDao.getEntityInfoById(company.getId(), EntityType.COMPANY);
        assert entityViewable != null;
        assert entityViewable.getEntityVote().getUpVote();
        assert entityCharacteristics != null;
        assert entityCharacteristics.getEntity().getId().equals(company.getId());
        assert entityCharacteristics.getUpVoteCount() == companyUpVoteCount + 1;
    }
    
    @Test
    public void getTappedQnAsTest() {
        List<QnAEntity> qnAEntityList = entityDao.getTappedEntities("myId", EntityType.QNA, 0);
        assert qnAEntityList != null;
        assert qnAEntityList.size() == 1;
        assert qnAEntityList.get(0).getId().equals("qnaId");
    }
    
    @Test
    public void getViewedUsersTest() {
        List<User> users = entityDao.getViewedEntities("myId", EntityType.USER, 0);
        assert users != null;
        assert users.size() == 1;
        assert users.get(0).getId().equals("targetUser");
    }
    
    @Test
    public void getUpVotedCompaniesTest() {
        List<Company> companies = entityDao.getUpVotedEntities("myId", EntityType.COMPANY, 0);
        assert companies != null;
        assert companies.size() == 1;
        assert companies.get(0).getId().equals("companyId");
    }
    
    @Test
    public void testGetEntityInfoWithoutUserEntityRelation() {
        operations.findAllAndRemove(new Query(), UserEntityRelation.class);
        EntityViewable<User> entityViewable = entityDao.getEntityInfoById("targetUser", EntityType.USER, "myId");
        assert entityViewable != null;
    }
    
    @Test
    public void testGetEntitiesInfo() {
        // creating one more qna entity
        EntityCharacteristics<QnAEntity> qnAEntity = new EntityCharacteristics<QnAEntity>();
        QnAEntity qnA = new QnAEntity();
        qnA.setQuestion(new Question());
        qnA.setAnswer("");
        qnA.setId("qnaId_2");
        qnAEntity.setEntity(qnA);
        qnAEntity.setType(EntityType.QNA);
        qnAEntity.setTapCount(qnaTapCount);
        qnAEntity.setUpVoteCount(qnaUpVoteCount);
        qnAEntity.setViewCount(qnaViewCount);
        try {
            entityDao.save(qnAEntity);
            UserEntityRelation<QnAEntity> userEntityRelation = new UserEntityRelation<QnAEntity>();
            userEntityRelation.setEntity(qnA);
            UserEntityRelation.EntityTap entityTap = new UserEntityRelation.EntityTap();
            entityTap.setFolderName("myFolder");
            entityTap.setTap(true);
            userEntityRelation.setEntityTap(entityTap);
            userEntityRelation.setUserId("myId");
            userEntityRelation.setType(EntityType.QNA);
            entityDao.save(userEntityRelation);
            List<EntityViewable<QnAEntity>> qnaViewables = entityDao.getEntityInfoByIds(Arrays.asList("qnaId", "qnaId_2"), EntityType.QNA, "myId");
            assert qnaViewables.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetEntitiesInfoWithNoRelation() {
        // creating one more qna entity
        EntityCharacteristics<QnAEntity> qnAEntity = new EntityCharacteristics<QnAEntity>();
        QnAEntity qnA = new QnAEntity();
        qnA.setQuestion(new Question());
        qnA.setAnswer("");
        qnA.setId("qnaId_2");
        qnAEntity.setEntity(qnA);
        qnAEntity.setType(EntityType.QNA);
        qnAEntity.setTapCount(qnaTapCount);
        qnAEntity.setUpVoteCount(qnaUpVoteCount);
        qnAEntity.setViewCount(qnaViewCount);
        try {
            entityDao.save(qnAEntity);
            List<EntityViewable<QnAEntity>> qnaViewables = entityDao.getEntityInfoByIds(Arrays.asList("qnaId", "qnaId_2"), EntityType.QNA, "myId");
            assert qnaViewables.size() == 2;
            EntityViewable<QnAEntity> emptyEntityViewable = null;
            for (EntityViewable<QnAEntity> entityViewable : qnaViewables) {
                if (entityViewable.getEntityCharacteristics().getEntity().getId().equals("qnaId_2")) {
                    emptyEntityViewable = entityViewable;
                }
            }
            assert emptyEntityViewable != null;
            assert emptyEntityViewable.getEntityTap() == null;
            assert emptyEntityViewable.getEntityView() == null;
            assert emptyEntityViewable.getEntityVote() == null;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @After
    public void deleteEntities() {
        operations.findAllAndRemove(new Query(), EntityCharacteristics.class);
        operations.findAllAndRemove(new Query(), UserEntityRelation.class);
    }
    
}
