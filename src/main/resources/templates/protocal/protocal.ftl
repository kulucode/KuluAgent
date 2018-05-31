<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#import "/common.ftl" as common>
    <#include "/meta.ftl">
    <@common.title name="协议列表" />
    <#include "/head.ftl">
</head>
<body>
<#include "/fileupload.ftl">
<div class="container">
    <@common.nav menu="protocal" submenu="protocal" />

    <div id="toolbar" class="btn-group">
        <button id="btn_add" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
        </button>
        <button id="btn_delete" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
        </button>
        <button id="btn_batch" type="button" class="btn btn-default">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>批量导入
        </button>
        <a type="button" class="btn btn-default" href="/file/template/protocal.do">
            <span class="glyphicon glyphicon-cloud-download" aria-hidden="true"></span>模板下载
        </a>
    </div>
    <table id="table"></table>
</div>

<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="addModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="addModalLabel">新增</h4>
            </div>
            <div class="modal-body">
                <form id="form">
                    <div class="form-group">
                        <label for="name">名称</label>
                        <input type="text" name="name" class="form-control" id="name" placeholder="协议名称" required autofocus="autofocus">
                    </div>
                    <div class="form-group">
                        <label for="start_flag">起始标记</label>
                        <input type="text" name="startFlag" class="form-control" id="startFlag" placeholder="协议消息的起始标记" required>
                    </div>
                    <div class="form-group">
                        <label for="end_flag">结束标记</label>
                        <input type="text" name="endFlag" class="form-control" id="endFlag" placeholder="协议消息的结束标记" required>
                    </div>
                    <div class="form-group">
                        <label for="offset_type">位移类型</label>
                        <select name="offset_type" class="form-control" id="offset_type">
                            <option value="1">分隔符</option>
                            <option value="2">偏移量</option>
                        </select>
                    </div>
                    <div class="form-group" name="split">
                        <label for="split">分隔符</label>
                        <input type="text" name="split" class="form-control" id="split" placeholder="一条完整的消息中，各字段以指定分隔符分割" required>
                    </div>
                    <div class="form-group hide" name="offset">
                        <label for="offset">偏移量</label>
                        <input type="number" name="offset" class="form-control" id="offset" placeholder="一条完整的消息中，提取字段的起始偏移量">
                    </div>
                    <div class="form-group">
                        <label for="length">长度</label>
                        <input type="number" name="length" class="form-control" id="length" placeholder="一条完整的消息中，提取字段的长度(字节数)或数组索引(从1开始)" required>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
                        <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<#include "/tail.ftl">
<script>
    $(function() {
        createBootstrapTable('#table', '/protocal/protocal.do', ['name', 'startFlag', 'endFlag', 'split', 'offset', 'length'], ['名称', '起始标记', '结束标记', '分隔符', '偏移量', '长度'], true, '#toolbar', 'GET');
        createFileInput('#pupload', '/file/template/protocal.do', function (event, data, previewId, index) {
            if (data.response.success) {
                $('#table').bootstrapTable('refresh', {silent: true});
                $("#fileinputModal").modal("hide");
            } else {
                toastr.error(data.response.message);
            }
        });

        $("#btn_batch").click(function() {
            $("#pupload").fileinput('refresh');
            $("#fileinputModal").modal("show");
        });
        $("#btn_add").click(function() {
            $("#form")[0].reset();
            $("div[name=split]").removeClass("hide");
            $("div[name=offset]").addClass("hide");
            $("#split").attr("required", "required");
            $("#offset").removeAttr("required")
            $("#addModalLabel").text("新增");
            $('#addModal').modal();
        });
        $("#btn_delete").click(function() {
            var selections = $('#table').bootstrapTable('getSelections');
            if (selections.length == 0) {
                toastr.error("请选择删除的记录!");
            } else {
                swal({
                    title: '确认要删除吗?',
                    text: "删除后，不可恢复!",
                    icon: "warning",
                    dangerMode: true,
                    buttons: {
                        cancel: true,
                        confirm: true,
                    },
                }).then((willDelete) => {
                    if (willDelete) {
                        $.ajax({
                            url: "/protocal/protocal.do",
                            type: "DELETE",
                            data: JSON.stringify(selections),
                            contentType:"application/json; charset=UTF-8",
                            dataType:"json",
                            success: function(result) {
                                if (result.success) {
                                    toastr.success('记录已删除.');
                                    $('#table').bootstrapTable('refresh', {silent: true});
                                } else {
                                    toastr.error(result.message);
                                }
                            }
                        });
                    }
                }).catch(swal.noop);
            }
        });
        $("#form").submit(function(){
            var params = $(this).serialize();
            $.post("/protocal/protocal.do", params, function(result) {
                if (result.success) {
                    toastr.success('保存成功.');
                    $('#table').bootstrapTable('refresh', {silent: true});
                    $('#addModal').modal('hide');
                } else {
                    toastr.error(result.message);
                }
            }, "json");
            return false;
        });
        $("#offset_type").change(function() {
            var val = $(this).val();
            if (val === "1") {
                $("div[name=split]").removeClass("hide");
                $("div[name=offset]").addClass("hide");
                $("#split").attr("required", "required");
                $("#offset").removeAttr("required")
            } else if (val === "2") {
                $("div[name=offset]").removeClass("hide");
                $("div[name=split]").addClass("hide");
                $("#offset").attr("required", "required");
                $("#split").removeAttr("required")
            }
        })
    });
</script>
</body>
</html>