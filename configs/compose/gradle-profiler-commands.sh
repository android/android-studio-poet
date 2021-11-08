#!/bin/sh
gradle-profiler --benchmark --project-dir ../generated_projects/compose/composeProject --scenario-file configs/compose/gradle-profiler.scenarios --output-dir profile-compose-clean-build clean_build
gradle-profiler --benchmark --project-dir ../generated_projects/compose/plainProject --scenario-file configs/compose/gradle-profiler.scenarios --output-dir profile-plain-clean-build clean_build
gradle-profiler --benchmark --project-dir ../generated_projects/compose/viewBindingProject --scenario-file configs/compose/gradle-profiler.scenarios --output-dir profile-viewbinding-clean-build clean_build
gradle-profiler --benchmark --project-dir ../generated_projects/compose/dataBindingProject --scenario-file configs/compose/gradle-profiler.scenarios --output-dir profile-databinding-clean-build clean_build
gradle-profiler --benchmark --project-dir ../generated_projects/compose/dataBindingKaptProject --scenario-file configs/compose/gradle-profiler.scenarios --output-dir profile-databindingkapt-clean-build clean_build

gradle-profiler --benchmark --project-dir ../generated_projects/compose/composeProject --scenario-file configs/compose/gradle-profiler.scenarios --output-dir profile-compose-inc-build compose_inc_build
gradle-profiler --benchmark --project-dir ../generated_projects/compose/plainProject --scenario-file configs/compose/gradle-profiler.scenarios --output-dir profile-plain-inc-build ui_inc_build
gradle-profiler --benchmark --project-dir ../generated_projects/compose/viewBindingProject --scenario-file configs/compose/gradle-profiler.scenarios --output-dir profile-viewbinding-inc-build ui_inc_build
gradle-profiler --benchmark --project-dir ../generated_projects/compose/dataBindingProject --scenario-file configs/compose/gradle-profiler.scenarios --output-dir profile-databinding-inc-build ui_inc_build
gradle-profiler --benchmark --project-dir ../generated_projects/compose/dataBindingKaptProject --scenario-file configs/compose/gradle-profiler.scenarios --output-dir profile-databindingkapt-inc-build ui_inc_build

gradle-profiler --profile buildscan --project-dir ../generated_projects/compose/composeProject --scenario-file configs/compose/gradle-profiler.scenarios clean_build
gradle-profiler --profile buildscan --project-dir ../generated_projects/compose/plainProject --scenario-file configs/compose/gradle-profiler.scenarios clean_build
gradle-profiler --profile buildscan --project-dir ../generated_projects/compose/viewBindingProject --scenario-file configs/compose/gradle-profiler.scenarios clean_build
gradle-profiler --profile buildscan --project-dir ../generated_projects/compose/dataBindingProject --scenario-file configs/compose/gradle-profiler.scenarios clean_build
gradle-profiler --profile buildscan --project-dir ../generated_projects/compose/dataBindingKaptProject --scenario-file configs/compose/gradle-profiler.scenarios clean_build

gradle-profiler --profile buildscan --project-dir ../generated_projects/compose/composeProject --scenario-file configs/compose/gradle-profiler.scenarios compose_inc_build
gradle-profiler --profile buildscan --project-dir ../generated_projects/compose/plainProject --scenario-file configs/compose/gradle-profiler.scenarios ui_inc_build
gradle-profiler --profile buildscan --project-dir ../generated_projects/compose/viewBindingProject --scenario-file configs/compose/gradle-profiler.scenarios ui_inc_build
gradle-profiler --profile buildscan --project-dir ../generated_projects/compose/dataBindingProject --scenario-file configs/compose/gradle-profiler.scenarios ui_inc_build
gradle-profiler --profile buildscan --project-dir ../generated_projects/compose/dataBindingKaptProject --scenario-file configs/compose/gradle-profiler.scenarios ui_inc_build
