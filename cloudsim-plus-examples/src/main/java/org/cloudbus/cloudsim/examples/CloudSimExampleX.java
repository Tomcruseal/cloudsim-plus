/*This is a test for CloudSimExample2 in this folder
*/

package org.cloudbus.cloudsim.examples;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;

import java.util.ArrayList;
import java.util.List;

public class CloudSimExampleX{
    private List<Cloudlet> cloudletList;
    private List<Vm> vmList;

    public static void main(String[] args) { new CloudSimExampleX(); }


    public CloudSimExampleX(){
        System.out.println("starting simulation" + getClass().getSimpleName());
        //getClass(), getSimpleName()

        DatacenterSimple datacenter1 = createDatacenter();
        //create a datacenter

        DatacenterBroker broker = new DatacenterBrokerSimple(simulation);

        vmList = new ArrayList<>();

        int vmid = 0;
        int mips = 250;
        int pesNumber = 1;
        long size = 10000;
        int ram = 1024;
        long bw = 1000;


        Vm vm1 = new VmSimple(++vmid, mips, pesNumber)
            .setRam(ram).setBw(bw).setSize(size).setCloudletScheduler(new CloudletSchedulerTimeShared());

        Vm vm2 = new VmSimple(++vmid, mips, pesNumber)
            .setRam(ram).setBw(bw).setSize(size).setCloudletScheduler(new CloudletSchedulerTimeShared());

        Vm vm3 = new VmSimple(++vmid, mips, pesNumber)
            .setRam(ram).setBw(bw).setSize(size).setCloudletScheduler(new CloudletSchedulerTimeShared());

        vmList.add(vm1);
        vmList.add(vm2);
        vmList.add(vm3);

        broker.submitVmList(vmList);

        cloudletList = new ArrayList<>();

        int clid = 0;
        long length = 250000;
        long fileSize = 300;
        long outputSize = 300;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        Cloudlet cloudlet1 =
            new CloudletSimple(++clid, length, pesNumber)
                .setFileSize(fileSize)
                .setOutputSize(outputSize)
                .setUtilizationModel(utilizationModel);

        Cloudlet cloudlet2 =
            new CloudletSimple(++clid, length, pesNumber)
                .setFileSize(fileSize)
                .setOutputSize(outputSize)
                .setUtilizationModel(utilizationModel);

        Cloudlet cloudlet3 =
            new CloudletSimple(++clid, length, pesNumber)
                .setFileSize(fileSize)
                .setOutputSize(outputSize)
                .setUtilizationModel(utilizationModel);

        cloudletList.add(cloudlet1);
        cloudletList.add(cloudlet2);
        cloudletList.add(cloudlet3);

        broker.submitCloudletList(cloudletList);

        broker.bindCloudletToVm(cloudlet1, vm1);
        broker.bindCloudletToVm(cloudlet2, vm2);
        broker.bindCloudletToVm(cloudlet3, vm3);

        simulation.start();

        List<Cloudlet> newList = broker.getCloudletFinishedList();
        new CloudletsTableBuilder(newList).build();
        System.out.println("simulation" + getClass().getSimpleName() + "finished");
    }

    private CloudSim simulation;

    private DatacenterSimple createDatacenter(){
        List<Host> hostList = new ArrayList<>();
        List<Pe> peList = new ArrayList<>();
        long mips = 1000;

        peList.add(new PeSimple(mips, new PeProvisionerSimple()));

        int hostid = 0;
        int ram = 4096;
        long storage = 1000000;
        long bw = 10000;

        Host host = new HostSimple(ram, bw, storage, peList)
            .setRamProvisioner(new ResourceProvisionerSimple())
            .setBwProvisioner(new ResourceProvisionerSimple())
            .setVmScheduler(new VmSchedulerTimeShared());

        hostList.add(host);

        DatacenterSimple dc = new DatacenterSimple(simulation, hostList, new VmAllocationPolicySimple());
        dc.setSchedulingInterval(1);

        return dc;

    }



}
