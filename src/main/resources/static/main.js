(function () {
    function init(table, url, fields, titles, hasCheckbox, toolbar, method) {
        $(table).bootstrapTable({
            url: url,                           //请求后台的URL（*）
            method: method,                     //请求方式（*）
            toolbar: toolbar,                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                    //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: queryParams,           //传递参数（*），这里应该返回一个object，即形如{param1:val1,param2:val2}
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 20,                       //每页的记录行数（*）
            pageList: [20, 50, 100],            //可供选择的每页的行数（*）
            search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            contentType: "application/json",
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            columns: createCols(fields, titles, hasCheckbox)
        });
    }
    function createCols(fields, titles, hasCheckbox) {
        if(fields.length != titles.length)
            return null;
        var arr = [];
        if (hasCheckbox) {
            var objc = {};
            objc.checkbox = true;
            arr.push(objc);
        }
        for (var i = 0;i < fields.length; i++) {
            var obj = {};
            obj.field = fields[i];
            obj.title = titles[i];
            arr.push(obj);
        }
        return arr;
    }
    //可发送给服务端的参数：limit->pageSize,offset->pageNumber,search->searchText,sort->sortName(字段),order->sortOrder('asc'或'desc')
    function queryParams(params) {
        return {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            search: params.search//关键字查询
        };
    }
    // 传'#table'
    createBootstrapTable = function(table, url, fields, titles, hasCheckbox, toolbar, method) {
        init(table, url, fields, titles, hasCheckbox, toolbar, method);
    }
})();

(function() {
    function init(ctrlName, uploadUrl, callback) {
        var control = $(ctrlName);

        //初始化上传控件的样式
        control.fileinput({
            language: 'zh', //设置语言
            uploadUrl: uploadUrl, //上传的地址
            allowedPreviewTypes: [],
            allowedFileExtensions: ['csv', 'txt'],//接收的文件后缀
            showUpload: true, //是否显示上传按钮
            showCaption: false,//是否显示标题
            browseClass: "btn btn-default", //按钮样式
            maxFileCount: 10, //表示允许同时上传的最大文件个数
            enctype: 'multipart/form-data',
            validateInitialCount:true,
            previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
            msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
        });
        //导入文件上传完成之后的事件
        control.on("fileuploaded", callback);
    }

    createFileInput = function (ctrlName, uploadUrl, callback) {
        init(ctrlName, uploadUrl, callback);
    }
})();