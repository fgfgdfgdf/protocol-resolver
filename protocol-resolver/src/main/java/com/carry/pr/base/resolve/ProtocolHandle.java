package com.carry.pr.base.resolve;

import com.carry.pr.base.tcp.TaskContent;
import com.carry.pr.base.tcp.TcpReadTask;
import com.carry.pr.base.tcp.TcpWriteTask;

public interface ProtocolHandle<in extends MsgReqObj, out extends MsgRespObj> extends TcpReadTask.ReadHandle, TcpWriteTask.WriteHandle {

    in getInObj(TaskContent content);

    out getOutObj(TaskContent content);

}
