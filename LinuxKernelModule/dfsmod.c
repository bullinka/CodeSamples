/*Karen Bullinger
  DFS Process Traversal
  CSCE 444, Spring 2016
  3/17/2016
  Sources: OS Concepts Essentials (3rd ed), Carl Derline, George Hauser
  This kernel module does a depth first traversal of the current processes running
  in the system when the kernel module is loaded.  The output is printed to the kernel log
  and can be accessed with dmesg.
  */

#include <linux/init.h>
#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/sched.h>
#include <linux/list.h>

struct task_struct *task;

//Does a depth first search traversal on v
void dfs(struct task_struct *v){
	struct list_head *p;
	struct task_struct *ts;
	//macro
	list_for_each(p, &v->children){ //gets child of v and all of that child's siblings
		ts = list_entry(p, struct task_struct, sibling);
		//Print the process name, PID, and state
		printk(KERN_INFO "%-15s\t%d\t%d\t%li\n", ts->comm, ts->pid, v->pid, ts->state);
		//Traverse the next task
		dfs(ts);
	}
}

int simple_init(void)
{
	printk(KERN_INFO "Loading Process Module: DFS Traversal\n");
	printk(KERN_INFO "%-15s\t%s\t%s\t%s", "Task", "PID", "PPID", "State");
	//print information of first task before doing DFS on it
	printk(KERN_INFO "%-15s\t%d\t--\t%li", init_task.comm, init_task.pid, init_task.state);
	dfs(&init_task);
    return 0;
}


/* This function is called when the module is removed. */
void simple_exit(void) {
	printk(KERN_INFO "Removing Module\n");
}

/* Macros for registering module entry and exit points. */
module_init( simple_init );
module_exit( simple_exit );

MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("DFS Process Traversal");
MODULE_AUTHOR("KB");

