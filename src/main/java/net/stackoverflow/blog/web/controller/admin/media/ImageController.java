package net.stackoverflow.blog.web.controller.admin.media;

import net.stackoverflow.blog.common.Response;
import net.stackoverflow.blog.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 图片管理接口
 *
 * @author 凉衫薄
 */
@Controller
@RequestMapping(value = "/admin/media")
public class ImageController {

    @Value("${server.upload.path}")
    private String path;

    /**
     * 图片管理页面跳转
     *
     * @return 返回ModelAndView对象
     */
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public ModelAndView image() {
        ModelAndView mv = new ModelAndView();

        Map<String, List<String>> imageMap = new TreeMap<>();

        //遍历上传路径，存储在imageMap
        traverseFolder(path, imageMap);
        mv.addObject("map", imageMap);
        mv.setViewName("/admin/media/image");
        return mv;
    }

    /**
     * 图片删除接口
     *
     * @param url 图片url
     * @return
     */
    @RequestMapping(value = "/image/delete", method = RequestMethod.POST)
    @ResponseBody
    public Response delete(@RequestParam("url") String url) {
        Response response = new Response();
        url = url.replaceFirst("/upload", "");
        File file = new File(path, url);
        if (file.exists()) {
            file.delete();
            response.setStatus(Response.SUCCESS);
            response.setMessage("图片删除成功");
        } else {
            throw new BusinessException("图片删除失败");
        }
        return response;
    }

    /**
     * 递归遍历文件夹列出图片
     *
     * @param path     图片上传路径
     * @param imageMap 按日期存储图片路径的map对象
     */
    private void traverseFolder(String path, Map<String, List<String>> imageMap) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                return;
            } else {
                for (File sonFile : files) {
                    if (sonFile.isDirectory()) {
                        traverseFolder(sonFile.getAbsolutePath(), imageMap);
                    } else {
                        addUrlToMap(imageMap, sonFile.getAbsolutePath());
                    }
                }
            }
        }
    }

    /**
     * 图片url根据规则添加进map
     *
     * @param imageMap 按日期存储图片路径的map对象
     * @param path     图片绝对路径
     */
    private void addUrlToMap(Map<String, List<String>> imageMap, String path) {
        String str = File.separator.equals("/") ? "/" : "\\\\";
        String[] paths = path.split(str);
        String date = paths[paths.length - 4] + "/" + paths[paths.length - 3] + "/" + paths[paths.length - 2];
        String url = "/" + paths[paths.length - 5] + "/" + date + "/" + paths[paths.length - 1];
        if (imageMap.get(date) == null) {
            List<String> list = new ArrayList<>();
            list.add(url);
            imageMap.put(date, list);
        } else {
            List<String> list = imageMap.get(date);
            list.add(url);
            imageMap.put(date, list);
        }
    }
}
