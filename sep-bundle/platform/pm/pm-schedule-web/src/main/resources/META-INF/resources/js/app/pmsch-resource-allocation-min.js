Ext.define("PMSch.ResourceAllocationPanel",{extend:"Gnt.panel.Gantt",rightLabelField:"Responsible",highlightWeekends:true,showTodayLine:true,loadMask:true,cascadeChanges:true,initComponent:function(){Ext.apply(this,{lockedGridConfig:{width:502,title:"Resources",collapsible:true},lockedViewConfig:{getRowClass:function(a){return a.isRoot()?"root-row":""}},schedulerConfig:{collapsible:true,title:"Resource Allocation"},leftLabelField:{dataIndex:"ResourceName"},rightLabelField:{dataIndex:"ProjectTitle"},eventRenderer:function(b){var a=this.resourceAllocationData.projectId;if(a&&a!=b.data.ProjectId&&b.data.leaf){return{style:"background-color:#F6CED8"}}}.bind(this),columns:[{header:"Project Title",dataIndex:"ProjectTitle",align:"right",width:100,renderer:function(b,f,e){var a=e.data.ProjectId;if(a){var c=EMF.bookmarks.buildLink("projectinstance",a);var d='<a href="'+c+'" target="_blank">'+b+"</a>";return d}return b}},{xtype:"treecolumn",header:"Resource Name",sortable:true,dataIndex:"ResourceName",width:200,field:{allowBlank:false},renderer:function(a,e,d){if(!d.data.leaf){e.tdCls="sch-gantt-parent-cell"}else{var b=EMF.bookmarks.buildLink(d.data.cls,d.data.aiid);var c='<a href="'+b+'" target="_blank">'+a+"</a>";return c}return a}},{header:"Status",dataIndex:"Status",width:100,align:"right"},{header:"Duration",xtype:"durationcolumn",width:100}],plugins:[Ext.create("Ext.grid.plugin.BufferedRenderer")],tooltipTpl:new Ext.XTemplate('<strong class="tipHeader">{Name}</strong>','<table class="taskTip">','<tr><td>Start:</td> <td align="right">{[values._record.getDisplayStartDate("y-m-d")]}</td></tr>','<tr><td>End:</td> <td align="right">{[values._record.getDisplayEndDate("y-m-d")]}</td></tr>','<tr><td>Progress:</td><td align="right">{[ Math.round(values.PercentDone)]}%</td></tr>',"</table>"),tbar:new PMSch.ResourceAllocationToolbar({gantt:this})});this.callParent(arguments);this.on("afterlayout",this.triggerLoad,this,{single:true,delay:100});this.taskStore.on({load:function(){this.body.unmask()},save:this.showLoadingMask,commit:this.showLoadingMask,scope:this})},showLoadingMask:function(){this.body.mask("Loading...",".x-mask-loading")},triggerLoad:function(){this.showLoadingMask();this.taskStore.loadDataSet()}});Ext.define("PMSch.ResourceAllocationToolbar",{extend:"Ext.Toolbar",gantt:null,initComponent:function(){var b=this.gantt;var a=[{xtype:"buttongroup",title:"View tools",columns:6,items:[{iconCls:"icon-prev",text:"Previous",scope:this,handler:function(){b.shiftPrevious()}},{iconCls:"icon-next",text:"Next",scope:this,handler:function(){b.shiftNext()}},{text:"Expand all",iconCls:"icon-expandall",scope:this,handler:function(){b.expandAll()}},{text:"Collapse all",iconCls:"icon-collapseall",scope:this,handler:function(){b.collapseAll()}},{text:"Zoom to fit",iconCls:"zoomfit",scope:this,handler:function(){b.zoomToFit()}},{text:"View full screen",iconCls:"icon-fullscreen",disabled:!this._fullScreenFn,scope:this,handler:function(){this.showFullScreen()}}]},{xtype:"buttongroup",title:"View resolution",columns:4,items:[{text:"6 weeks",scope:this,handler:function(){b.switchViewPreset("weekAndMonth")}},{text:"10 weeks",scope:this,handler:function(){b.switchViewPreset("weekAndDayLetter")}},{text:"1 year",scope:this,handler:function(){b.switchViewPreset("monthAndYear")}},{text:"5 years",scope:this,handler:function(){var d=new Date(b.getStart().getFullYear(),0);b.switchViewPreset("monthAndYear",d,Ext.Date.add(d,Ext.Date.YEAR,5))}}]}];if(b.resourceAllocationData.projectId){var c=Ext.create("Ext.Button",{text:"View allocations on other projects",iconCls:"togglebutton",enableToggle:true,handler:function(d){b.resourceAllocationData.viewOtherProjects=d.pressed;b.triggerLoad()}});c.toggle(b.resourceAllocationData.viewOtherProjects,true);a.push({xtype:"buttongroup",title:"Filters",columns:1,items:[c]})}Ext.apply(this,{items:a});this.callParent(arguments)},showFullScreen:function(){this.gantt.el.down(".x-panel-body").dom[this._fullScreenFn]()},_fullScreenFn:(function(){var a=document.documentElement;if(a.requestFullscreen){return"requestFullscreen"}else{if(a.mozRequestFullScreen){return"mozRequestFullScreen"}else{if(a.webkitRequestFullScreen){return"webkitRequestFullScreen"}}}})()});Ext.define("PMSch.model.PMResourceAllocationModel",{extend:"Gnt.model.Task",autoLoad:false,autoSync:false,nameField:"ResourceName",fields:[{name:"aiid",type:"string"},{name:"Status",type:"string"},{name:"ProjectTitle",type:"string"},{name:"ProjectId",type:"string"}]});Ext.ns("PM");Ext.Loader.setConfig({enabled:true,disableCaching:true,paths:{Gnt:"../libs/extGantt",Sch:"../libs/extSch",PMSch:"../js/app/"}});;Ext.onReady(function(){PM.ResourceAllocation.init()});PM.ResourceAllocation={scheduleConfig:{},init:function(b){this.gantt=this.createGantt(PM.Sch.resourceAllocationData);var a=Ext.create("Ext.panel.Panel",{layout:"fit",renderTo:"resourceAllocationContainer",height:900,items:this.gantt})},createGantt:function(c){var a=Ext.create("Gnt.data.TaskStore",{model:"PMSch.model.PMResourceAllocationModel",rootVisible:false,root:{expanded:false},proxy:{type:"memory"},loadDataSet:function(){var d="/emf/service/resourceAllocation/task/filter";Ext.Ajax.request({url:d,method:"POST",jsonData:c,success:this.populateDataStore,callback:function(){this.fireEvent("load")},scope:this})},populateDataStore:function(d){var e=Ext.decode(d.responseText);this.setRootNode(e)}});var b=Ext.create("PMSch.ResourceAllocationPanel",{layout:"border",region:"center",rowHeight:26,selModel:new Ext.selection.TreeModel({ignoreRightMouseSelection:false,mode:"MULTI"}),taskStore:a,resizeHandles:"none",enableTaskDragDrop:false,enableDependencyDragDrop:false,columnLines:false,rowLines:false,autoFitOnLoad:true,showTodayLine:true,viewPreset:"weekAndDayLetter",resourceAllocationData:c});return b}};