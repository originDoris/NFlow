package com.doris.nflow.engine.node.instance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doris.nflow.engine.common.exception.ParamException;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceLogType;
import com.doris.nflow.engine.node.instance.enumerate.NodeInstanceStatus;
import com.doris.nflow.engine.node.instance.model.NodeInstance;
import com.doris.nflow.engine.node.instance.model.NodeInstanceLog;
import com.doris.nflow.engine.node.instance.model.NodeInstanceLogQuery;
import com.doris.nflow.engine.node.instance.model.NodeInstanceQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author: xhz
 * @Title: NodeInstanceLogService
 * @Description:
 * @date: 2022/9/30 08:59
 */
public interface NodeInstanceLogService {

    /**
     * 保存节点实例日志日志数据
     *
     * @param nodeInstanceLog
     * @return
     */
    boolean save(@Valid @NotNull(message = "节点实例日志日志对象不能为空！") NodeInstanceLog nodeInstanceLog);


    /**
     * 更新节点实例日志数据
     * @param nodeInstanceLog
     * @return
     */
    boolean modify(@Valid @NotNull(message = "节点实例日志对象不能为空！") NodeInstanceLog nodeInstanceLog);


    /**
     * 删除节点实例日志数据 逻辑删除
     * @param id 节点实例日志id
     * @return
     */
    boolean remove(@NotNull(message = "节点实例日志id不能为空！") Long id);


    Optional<NodeInstanceLog> detail(@NotNull(message = "节点实例日志id不能为空！")Long id);

    /**
     * 查询节点实例日志列表
     * @param nodeInstanceLogQuery
     * @return
     */
    List<NodeInstanceLog> queryList(@NotNull(message = "查询参数不能为空！") NodeInstanceLogQuery nodeInstanceLogQuery);


    /**
     * 查询节点实例日志列表 分页
     * @param nodeInstanceLogQuery
     * @return
     */
    IPage<NodeInstanceLog> queryPage(@NotNull(message = "查询参数不能为空！") NodeInstanceLogQuery nodeInstanceLogQuery);


    boolean replace(@NotEmpty(message = "节点实例日志列表不能为空！") List<NodeInstanceLog> nodeInstanceLogs);
}
