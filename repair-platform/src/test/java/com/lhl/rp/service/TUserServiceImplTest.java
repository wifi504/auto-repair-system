package com.lhl.rp.service;

import com.lhl.rp.bean.TUser;
import com.lhl.rp.repository.TUserMapper;
import com.lhl.rp.service.impl.TUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author WIFI连接超时
 * @version 1.0
 * Create Time 2025/4/1_22:24
 */
@ExtendWith(MockitoExtension.class)
public class TUserServiceImplTest {
    @Mock
    private TUserMapper tUserMapper;

    @InjectMocks
    private TUserServiceImpl tUserService;

    private TUser testUser;
    private List<TUser> testUserList;

    @BeforeEach
    void setUp() {
        testUser = new TUser();
        testUser.setId(1L);
        testUser.setLoginAct("testUser");
        testUser.setLoginPwd("password");
        testUser.setRealName("Test User");
        testUser.setStatus((byte) 1);
        testUser.setCreateTime(new Date());

        TUser testUser2 = new TUser();
        testUser2.setId(2L);
        testUser2.setLoginAct("testUser2");

        testUserList = Arrays.asList(testUser, testUser2);
    }

    @Test
    void selectAll() {
        when(tUserMapper.selectAll()).thenReturn(testUserList);

        List<TUser> result = tUserService.selectAll();

        assertEquals(2, result.size());
        verify(tUserMapper, times(1)).selectAll();
    }

    @Test
    void selectByName() {
        when(tUserMapper.selectByLoginAct("testUser")).thenReturn(testUser);

        TUser result = tUserService.selectByName("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getLoginAct());
        verify(tUserMapper, times(1)).selectByLoginAct("testUser");
    }

    @Test
    void selectById() {
        when(tUserMapper.selectByPrimaryKey(1L)).thenReturn(testUser);

        TUser result = tUserService.selectById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(tUserMapper, times(1)).selectByPrimaryKey(1L);
    }

    @Test
    void deleteById_Success() {
        when(tUserMapper.deleteByPrimaryKey(1L)).thenReturn(1);

        int result = tUserService.deleteById(1L);

        assertEquals(1, result);
        verify(tUserMapper, times(1)).deleteByPrimaryKey(1L);
    }

    @Test
    void deleteById_UserNotExist() {
        when(tUserMapper.deleteByPrimaryKey(1L)).thenReturn(0);

        assertThrows(RuntimeException.class, () -> tUserService.deleteById(1L));
        verify(tUserMapper, times(1)).deleteByPrimaryKey(1L);
    }

    @Test
    void deleteByIds_Success() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(tUserMapper.deleteByPrimaryKeys(ids)).thenReturn(2);

        int result = tUserService.deleteByIds(ids);

        assertEquals(2, result);
        verify(tUserMapper, times(1)).deleteByPrimaryKeys(ids);
    }

    @Test
    void deleteByIds_PartialFailure() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(tUserMapper.deleteByPrimaryKeys(ids)).thenReturn(1);

        assertThrows(RuntimeException.class, () -> tUserService.deleteByIds(ids));
        verify(tUserMapper, times(1)).deleteByPrimaryKeys(ids);
    }

    @Test
    void insert_Success() {
        when(tUserMapper.selectByPrimaryKey(1L)).thenReturn(null);
        when(tUserMapper.insert(testUser)).thenReturn(1);

        int result = tUserService.insert(testUser);

        assertEquals(1, result);
        verify(tUserMapper, times(1)).selectByPrimaryKey(1L);
        verify(tUserMapper, times(1)).insert(testUser);
    }

    @Test
    void insert_UserAlreadyExists() {
        when(tUserMapper.selectByPrimaryKey(1L)).thenReturn(testUser);

        assertThrows(RuntimeException.class, () -> tUserService.insert(testUser));
        verify(tUserMapper, times(1)).selectByPrimaryKey(1L);
        verify(tUserMapper, never()).insert(any());
    }

    @Test
    void insert_Failure() {
        when(tUserMapper.selectByPrimaryKey(1L)).thenReturn(null);
        when(tUserMapper.insert(testUser)).thenReturn(0);

        assertThrows(RuntimeException.class, () -> tUserService.insert(testUser));
        verify(tUserMapper, times(1)).selectByPrimaryKey(1L);
        verify(tUserMapper, times(1)).insert(testUser);
    }

    @Test
    void updateById_Success() {
        when(tUserMapper.updateByPrimaryKeySelective(testUser)).thenReturn(1);

        int result = tUserService.updateById(testUser);

        assertEquals(1, result);
        verify(tUserMapper, times(1)).updateByPrimaryKeySelective(testUser);
    }

    @Test
    void updateById_Failure() {
        when(tUserMapper.updateByPrimaryKeySelective(testUser)).thenReturn(0);

        assertThrows(RuntimeException.class, () -> tUserService.updateById(testUser));
        verify(tUserMapper, times(1)).updateByPrimaryKeySelective(testUser);
    }
}
