package org.apache.shenyu.admin.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import org.apache.shenyu.admin.mapper.FieldMapper;
import org.apache.shenyu.admin.model.dto.FieldDTO;
import org.apache.shenyu.admin.model.entity.FieldDO;
import org.apache.shenyu.admin.model.page.CommonPager;
import org.apache.shenyu.admin.model.page.PageParameter;
import org.apache.shenyu.admin.model.query.FieldQuery;
import org.apache.shenyu.admin.model.vo.FieldVO;
import org.apache.shenyu.admin.service.impl.FieldServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

/**
 * test for {@linkplain FieldServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FieldServiceTest {

    @InjectMocks
    private FieldServiceImpl fieldService;

    @Mock
    private FieldMapper fieldMapper;

    @Test
    public void testCreateOrUpdate() {
        final FieldDTO fieldDTO = new FieldDTO();
        fieldDTO.setId("mock field id");

        reset(fieldMapper);
        when(fieldMapper.updateByPrimaryKeySelective(any())).thenReturn(1);
        when(fieldMapper.insert(any())).thenReturn(1);

        int result = fieldService.createOrUpdate(fieldDTO);
        assertEquals(1, result);

        fieldDTO.setId(null);
        result = fieldService.createOrUpdate(fieldDTO);
        assertEquals(1, result);
    }

    @Test
    public void testDelete() {
        reset(fieldMapper);
        when(fieldMapper.deleteByPrimaryKey(anyString())).thenReturn(1);

        int result = fieldService.delete("id");
        assertEquals(1, result);
    }

    @Test
    public void testDeleteBatch() {
        List<String> ids = new ArrayList<>();
        ids.add("mock id 1");
        ids.add("mock id 2");

        reset(fieldMapper);
        when(fieldMapper.batchDelete(anyList())).thenReturn(2);

        int result = fieldService.deleteBatch(ids);
        assertEquals(2, result);
    }

    @Test
    public void testFindById() {
        FieldDO fieldDO = new FieldDO();
        fieldDO.setId("mock test id");

        reset(fieldMapper);
        when(fieldMapper.selectByPrimaryKey(anyString())).thenReturn(fieldDO);

        FieldVO result = fieldService.findById("mock test id");
        assertNotNull(result);
        assertEquals(fieldDO.getId(), result.getId());
    }

    @Test
    public void testListByPage() {
        FieldQuery fieldQuery = new FieldQuery();
        PageParameter pageParameter = new PageParameter();
        pageParameter.setPageSize(10);
        pageParameter.setCurrentPage(1);
        fieldQuery.setPageParameter(pageParameter);

        List<FieldDO> fieldDOList = new ArrayList<>();
        FieldDO fieldDO = new FieldDO();
        fieldDO.setId("mock test id");
        fieldDOList.add(fieldDO);

        reset(fieldMapper);
        when(fieldMapper.selectByQuery(any())).thenReturn(fieldDOList);

        CommonPager<FieldVO> result = fieldService.listByPage(fieldQuery);
        assertNotNull(result);
        assertEquals(fieldDOList.size(), result.getDataList().size());
    }

}
