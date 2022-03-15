package cn.cimao.www.futureZiGe;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MainClass extends JavaPlugin {
    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }
        getLogger().info(" §b╔══  §e╦  §a╔╦╗  §4╔═╗  §2╔═╗ ");
        getLogger().info(" §b║    §e║  §a║║║  §4║ ║  §2║ ║ ");
        getLogger().info(" §b╚══  §e╩  §a╩ ╩  §4╚═╚  §2╚═╝ ");
        getLogger().info("MaoZiGe插件加载成功！");
        getLogger().info("作者Q 916397235");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //zg set [名称]
        if (args.length==2&&args[0].equals("create")&&sender.isOp()) {
            if (getConfig().get(args[1])==null) {
                getConfig().set(args[1], "一个资格的名称");
                saveConfig();
                sender.sendMessage("§f§l[§4§l服务器§f§l]§d创建成功");
                return true;
            }else {
                sender.sendMessage("§f§l[§4§l服务器§f§l]§c资格已存在，请先删除");
                return true;
            }
        }
        //zg give [玩家名字] [资格名称] [使用次数]
        if (args.length==4&&args[0].equals("give")&&sender.isOp()){
            if (getConfig().get(args[2])==null){
                sender.sendMessage("§f§l[§4§l服务器§f§l]§c还未创建此资格");
                return true;
            }
            if (getConfig().get(args[2]+"."+args[1])==null) {
                getConfig().set(args[2]+"."+args[1],Integer.parseInt(args[3]));
                sender.sendMessage("§f§l[§4§l服务器§f§l]§d资格添加成功");
                saveConfig();
                return true;
            }else if (getConfig().getInt(args[2]+"."+args[1])==-1) {
                sender.sendMessage("§f§l[§4§l服务器§f§l]§d此玩家已有永久资格");
            }else {
                getConfig().set(args[2]+"."+args[1],getConfig().getInt(args[2]+"."+args[1])+Integer.parseInt(args[3]));
                sender.sendMessage("§f§l[§4§l服务器§f§l]§d资格添加成功");
                saveConfig();
                return true;
            }
        }
        //zg remove [资格名称]
        if (args.length==2&&args[0].equals("remove")&&sender.isOp()) {
            if (getConfig().get(args[1])!=null) {
                getConfig().set(args[1], null);
                saveConfig();
                sender.sendMessage("§f§l[§4§l服务器§f§l]§d删除成功");
                return true;
            }else {
                sender.sendMessage("§f§l[§4§l服务器§f§l]§c无此资格，请查证");
                return true;
            }
        }
        //zg del [玩家名字] [资格名称]
        if (args.length==3&&args[0].equals("del")&&sender.isOp()){
            if (getConfig().get(args[2])==null){
                sender.sendMessage("§f§l[§4§l服务器§f§l]§c还未创建此资格");
                return true;
            }else {
                getConfig().set(args[2]+"."+args[1],null);
                saveConfig();
                sender.sendMessage("§f§l[§4§l服务器§f§l]§d资格删除成功");
                return true;
            }
        }
        //zg use [资格名称] [执行指令]
        if (args.length==3&&args[0].equals("use")&&sender.isOp()) {
            if (getConfig().get(args[1])==null) {
                sender.sendMessage("§f§l[§4§l服务器§f§l]§c还未创建此资格");
                return true;
            }else {
                Player player = (Player)sender;
                if (getConfig().getInt(args[1]+"."+sender.getName())!=0){
                    int num = getConfig().getInt(args[1]+"."+sender.getName());
                    if (num>0){
                        num--;
                        getConfig().set(args[1]+"."+sender.getName(),num);
                        saveConfig();
                    }
                    String cmd = args[2].replaceAll("_"," ").replaceAll("%p",player.getName());
                    Bukkit.dispatchCommand(player, cmd);
                    return true;
                }else {
                    sender.sendMessage("§f§l[§4§l服务器§f§l]§c您的使用次数已用尽");
                    return true;
                }
            }
        }
        //zg reload
        if (args.length==1&&args[0].equals("del")&&sender.isOp()){
            reloadConfig();
            sender.sendMessage("§f§l[§4§l服务器§f§l]§d重载成功");
            return true;
        }
        if (label.equalsIgnoreCase("zg")&&sender.isOp()) {
            sender.sendMessage("zg create [名称]");
            sender.sendMessage("§d§l创建一个新的资格");
            sender.sendMessage("zg give [玩家名字] [资格名称] [使用次数]");
            sender.sendMessage("§d§l使用次数必须为数字 -1为无限使用");
            sender.sendMessage("zg del [玩家名字] [资格名称]");
            sender.sendMessage("§d§l删除某个玩家的某项资格");
            sender.sendMessage("zg use [资格名称] [执行指令]");
            sender.sendMessage("§d§l执行资格命令 _代替空格%p代替玩家名");
            sender.sendMessage("zg remove [资格名称]");
            sender.sendMessage("§d§l删除这项资格");
            sender.sendMessage("zg reload");
            sender.sendMessage("§d§l重载插件");
        }
        return true;
    }
}
