# Releasing Nordic Common Libraries

A release is made by the **Release** GitHub Action. It generates the documentation, creates the tag
and publishes the libraries to Maven Central. The version of the release is the name of the tag,
e.g. `2.9.4`.

## How to release

1. Merge everything that should be released to `main`.
2. Open **Actions → Release → Run workflow**.
3. Choose `main` in **Use workflow from**.
4. Enter the **version**, e.g. `2.9.4`, and run the workflow. To try it first, tick **Dry run**.

The workflow runs in five stages. Each one is a job that depends on the previous one, so the graph of
the run shows which stage failed:

1. **Check.** Checks that it runs on a branch, that the version looks like `2.9.4` or
   `2.10.0-beta01`, and that the tag does not exist yet.
2. **Build.** Runs `./gradlew assembleDebug`. There are no unit tests in this repository.
3. **Docs.** Runs `./gradlew dokkaGenerate` for the given version and commits the result to the
   branch as `Docs for <version>`. The documentation is written to `docs/html`, which is published
   by GitHub Pages.
4. **Tag.** Creates the tag `<version>` on the commit with the documentation, and pushes it.
5. **Publish.** Builds the tag, publishes all the libraries and releases the staging repository.

Pushing a tag does not start any workflow. Publishing is a stage of the **Release** workflow.
After it finished, check that **Publish** succeeded, and create a release on GitHub from the tag.

## Dry run

With **Dry run** ticked all the stages run, but nothing leaves the runner: the documentation is not
committed, the tag is not pushed, and the libraries are only built and signed into the local Maven
repository of the runner (`./gradlew publishToMavenLocal`). Use it to check the workflow or the
secrets before a real release.

## How the version is set

The Nordic Gradle plugins use the version from the `releaseVersion` Gradle property, then the
`RELEASE_VERSION` environment variable, then the latest git tag, and `0.0.0` if none is available.
The **Release** workflow sets `RELEASE_VERSION`.

## Troubleshooting

* **A stage failed.** Fix the cause and use **Re-run failed jobs**. Stages that succeeded are not
  repeated, so the documentation commit and the tag stay.
* **Check: the tag already exists.** Delete it if nothing was published
  (`git push origin :refs/tags/<version>`), or use a new version.
* **Docs: the push was rejected.** The branch changed during the release. Run the workflow again.
