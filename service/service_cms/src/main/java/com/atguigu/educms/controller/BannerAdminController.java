package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-01-13
 */
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin
public class BannerAdminController {
    @Autowired
    private CrmBannerService bannerService;

    //1 分页查询banner
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page, @PathVariable long limit){
        Page<CrmBanner> pageBanner=new Page<>(page,limit);
        bannerService.page(pageBanner,null);
        return R.ok().data("items",pageBanner.getRecords()).data("total",pageBanner.getTotal());
    }

    //2 增加Banner
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner banner){
        bannerService.save(banner);
        return R.ok();
    }

    //3 修改banner

    //根据id查询banner
    @GetMapping("get/{id}")
    public R get(@PathVariable String id){
        CrmBanner banner=bannerService.getById(id);
        return R.ok().data("item",banner);
    }

    @PutMapping("update")
    public R updateById(@RequestBody CrmBanner banner){
        bannerService.updateById(banner);
        return R.ok();
    }

    //4 删除banner
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id){
        bannerService.removeById(id);
        return R.ok();
    }
}

