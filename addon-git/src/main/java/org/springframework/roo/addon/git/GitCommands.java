package org.springframework.roo.addon.git;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;

/**
 * Commands for addon-git.
 * 
 * @author Stefan Schmidt
 * @since 1.1
 */
@Component
@Service
public class GitCommands implements CommandMarker {
	
	@Reference private GitOperations revisionControl;
	
	@CliAvailabilityIndicator({"git config", "git commit all", "git revert last commit", "git revert commit"})
	public boolean isCommandAvailable() {
		return revisionControl.isGitCommandAvailable();
	}
	
	@CliAvailabilityIndicator("git setup")
	public boolean isSetupCommandAvailable() {
		return revisionControl.isSetupCommandAvailable();
	}
	
	@CliCommand(value="git setup", help="Setup Git revision control")
	public void config() {
		revisionControl.setup();
	}
	
	@CliCommand(value="git config", help="Git revision control configuration (.git/config)")
	public void config(@CliOption(key={"userName"}, mandatory=false, help="The user name") String userName,
			@CliOption(key={"email"}, mandatory=false, help="The user email") String email,
			@CliOption(key={"repoUrl"}, mandatory=false, help="The URL of the remote repository") String repoUrl,
			@CliOption(key={"colorCoding"}, mandatory=false, specifiedDefaultValue = "true", unspecifiedDefaultValue = "false", help="Enable color coding of commands in OS shell") boolean color) {
		
		if (userName != null && userName.length() > 0) {
			revisionControl.setConfig("user", "name", userName);
		}
		if (email != null && email.length() > 0) {
			revisionControl.setConfig("user", "email", email);
		}
		if (repoUrl != null && repoUrl.length() > 0) {
			revisionControl.setConfig("remote \"origin\"", "url", repoUrl);
		}
		if (color) {
			revisionControl.setConfig("color", "diff", "auto");
			revisionControl.setConfig("color", "branch", "auto");
			revisionControl.setConfig("color", "status", "auto");
		}
	}
	
	@CliCommand(value="git commit all", help="Trigger a commit manually for the project")
	public void config(@CliOption(key={"message"}, mandatory=true, help="The commit message") String message) {
		revisionControl.commitAllChanges(message);
	}
	
	@CliCommand(value="git revert last commit", help="Revert last commit")
	public void revertLast(@CliOption(key={"message"}, mandatory=true, help="The commit message") String message) {
		revisionControl.revertCommit(1, message);
	}
	
	@CliCommand(value="git revert commit", help="Revert commit")
	public void revert(@CliOption(key={"commitCount"}, mandatory=true, help="Number of commits to revert") int history,
			@CliOption(key={"message"}, mandatory=true, help="The commit message") String message) {
		revisionControl.revertCommit(history, message);
	}
}
