/**
 * This isn't int the pseudocode but it's not being used at all.
 * I had an initial idea but I scrapped that from the pseudocode and just left this code here to rot :/
 */

package com.shreyaslad.ed1t.Data;

import org.eclipse.jgit.lib.Ref;

import java.util.LinkedList;
import java.util.List;

public class BranchList {

    private static LinkedList<String> branchList = new LinkedList<>();
    private static String selectedBranch;

    public static void updateBranches(List<Ref> call) {
        while (branchList.size() > 0) {
            branchList.remove(0);
        }

        for (Ref ref : call) {
            branchList.add(ref.getName());
        }
    }

    public static String get(int index) {
        return branchList.get(index);
    }

    public static int size() {
        return branchList.size();
    }

    public static void remove(String branch) {
        branchList.remove(branch);
    }

    public static void setSelectedBranch(String branch) {
        if (!branchList.contains(branch)) {
            //ignore
        }
        selectedBranch = branch;
    }

    public static String getSelectedBranch() {
        return selectedBranch;
    }

}
