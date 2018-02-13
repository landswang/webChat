 $(function(){

        var $list = $("#thelist");
        var uploader;
    　　 // 初始化uploader
        uploader = WebUploader.create({
            auto:true, //不自动提交，需要点击
            pick: {
                id: '#attach',
                label: '图',
            },
            server: 'test/save', //后台接收服务
            resize: false,
            formData: {"Authorization": localStorage.token}, //额外参数传递，可以没有
            chunked: true, //分片
            chunkSize: 10 * 1024 * 1024, //分片大小指定
            threads:1, //线程数量
            disableGlobalDnd: false //禁用拖拽
        });

        //添加文件页面提示
        uploader.on( "fileQueued", function( file ) {
    	 $li = $(
    			"<div class='message-div row'>"+
		        "<div style='display: inline-block; margin-right: 5px;' >"+
		        "<img class='media-object f-image img-circle' style='position: absolute' src=" + $( "#_userInfoList").find('img').attr('src') + ">"+
		        "</div>"+
		        " <div style='margin-left: 55px'>"+
		        "<p class='user-nickname'>" + $( "#cUserNickName").text() + "</p> <p class='user-time'>"+dateFtt("yyyy-MM-dd hh:mm:ss",new Date())+"</p><br>"+
  		        '<div id="' + file.id + '" class="file-item thumbnail">' +   
  		        '<img>' +   
  		        //'<div class="info">' + file.name + '</div>' +   
  		        '</div></div></div><br>'
  		    ),   
  		    $img =  $( "#"+file.id ).find('img');
    	 	
  		    $list.append( $li ); // $list为容器jQuery实例    
  		    // 创建缩略图   
  		    uploader.makeThumb( file, function( error, src ) {   
  		        if ( error ) {   
  		            $img.replaceWith('<span>不能预览</span>');   
  		            return;   
  		        }      
  		        $img.attr( 'src', src );//设置预览图
  		    }, 740 ); //100x100为缩略图尺寸  
  		});
        
        //开进度条
        uploader.on( 'uploadProgress', function( file, percentage ) {
            var $li = $( '#'+file.id ),
                $percent = $li.find('.progress .progress-bar');
            if ( !$percent.length ) {
                $percent = $('<div class="progress progress-striped active">' +
                    '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                    '</div>' +
                    '</div>').appendTo( $li ).find('.progress-bar');
            }
            $li.find('p.state').text('上传中');
            $percent.css( 'width', percentage * 100 + '%' );
        });
        
        //上传失败
        uploader.on( "uploadError", function( file ) {
            $( "#"+file.id ).find("p.state").text("上传出错");
            uploader.cancelFile(file);
            uploader.removeFile(file,true);
        });
      	//上传成功
        uploader.on( "uploadSuccess", function( file ,response) {
        	$img = $( "#"+file.id ).find('img');
        	$img.attr( 'src', response.fileName );//设置预览图
        	//alert(response.fileName+" "+response.filemd5)
        	//var imggg = "<img src='"+response.fileName+"' width=50px />"
            //$( "#"+file.id ).find("p.state").html(imggg);
        	//alert(fileName)
            $('#' + file.id).find('.progress').fadeOut();
            sendmsg(response.fileName);
        	
        });
        //点击上传
        $("#upload").on("click", function() {
            uploader.upload();
        })

    });
    