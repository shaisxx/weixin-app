<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>地图位置</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
  <link rel="stylesheet" href="${context}/jssdk/css/style.css">
</head>
<body ontouchstart="">
<div class="wxapi_container">
    <div class="lbox_close wxapi_form">
    	<button class="btn btn_primary" id="openLocation">地图</button>
		<button class="btn btn_primary" id="getLocation">我的位置</button>
	</div>
</div>

</body>
<script src="${context}/jssdk/js/jweixin-1.0.0.js"></script>
<script>
  wx.config({
      debug: false,
      appId: 'wx7872670352c05f96',
      timestamp: '${siInfo.timestamp}',
      nonceStr: '${siInfo.noncestr}',
      signature: '${siInfo.signature}',
      jsApiList: [
		'openLocation',
        'getLocation'
      ]
  });
</script>
<script src="${context}/jssdk/js/zepto.min.js"></script>
<script>
  // 7 地理位置接口
  // 7.1 查看地理位置
  document.querySelector('#openLocation').onclick = function () {
    wx.openLocation({
      latitude: 23.099994,
      longitude: 113.324520,
      name: 'TIT 创意园',
      address: '广州市海珠区新港中路 397 号',
      scale: 14,
      infoUrl: 'http://weixin.qq.com'
    });
  };
  
  // 7.2 获取当前地理位置
  document.querySelector('#getLocation').onclick = function () {
    wx.getLocation({
      success: function (res) {
        alert(JSON.stringify(res));
      },
      cancel: function (res) {
        alert('用户拒绝授权获取地理位置');
      }
    });
  }; 
</script>
</html>