package deliverable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;


 
public class GetCommitInfo{
	
	public static ArrayList<RevCommit> commitList() throws IOException, NoHeadException, GitAPIException {
		ArrayList<RevCommit> ticketCommits= new ArrayList <RevCommit>();
		File dir= new File("/home/alessandro/eclipse-workspace/bookkeeper/.git");
		RepositoryBuilder build = new RepositoryBuilder();
		Repository rep = build.setGitDir(dir).readEnvironment().findGitDir().build();
		Git git = new Git(rep);
		Iterable<RevCommit> log = git.log().call();
		for(RevCommit r : log) {
			ticketCommits.add(r);	
		}
		git.close();
		return ticketCommits;
	}
}